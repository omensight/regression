package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.alphemsoft.education.regression.databinding.FragmentResultsBinding
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.ui.adapter.ResultAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class AbstractResultFragment : BaseFragment<FragmentResultsBinding, ResultViewModel>(
    layoutId = R.layout.fragment_results,
    viewModelId = BR.resultViewModel,
    menuResourceId = R.menu.menu_result
)

@AndroidEntryPoint
class ResultFragment : AbstractResultFragment() {
    override val viewModel: ResultViewModel by activityViewModels()
    val args: ResultFragmentArgs by navArgs()

    private val resultAdapter: ResultAdapter = ResultAdapter()
    private var started = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupResultList()
        val a = dataBinding.rvResults.adapter
        if (!started){
            coroutineHandler.backgroundScope.launch {
                refreshResultList()
            }
            started = true
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private suspend fun refreshResultList() {
        val unifiedNativeNativeAds: List<NativeAdEntity> = nativeAdDispatcher.fetchAds()
        viewModel.getDataPointsFlow(args.regressionId).collectLatest {data->
            val sheet = viewModel.getSheet(args.regressionId)
            sheet?.let {
                val regression = RegressionFactory.generateRegression(sheet.type)
                regression.setData(data)
                val results = regression.getResults(5)
                val resultsWithAds: MutableList<Any> = ArrayList()
                var adIndex = 0
                results.forEachIndexed { index, result ->
                    if (index % 5 == 0 && index > 0) {
                        val adEntity = NativeAdEntity()
                        if (unifiedNativeNativeAds.isNotEmpty()){
                            adEntity.unifiedNativeAd = unifiedNativeNativeAds[adIndex++].unifiedNativeAd
                            if (adIndex > unifiedNativeNativeAds.size -1){
                                adIndex = 0
                            }
                        }
                        resultsWithAds.add(adEntity)

                    }
                    resultsWithAds.add(result)

                }
                coroutineHandler.foregroundScope.launch {
                    resultAdapter.addNewItems(resultsWithAds)
                }
            }
        }

    }

    private fun setupResultList() {

        dataBinding.rvResults.apply {
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = resultAdapter
            addItemDecoration(DividerItemDecoration(16))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_plot -> {
                val action = ResultFragmentDirections.actionShowGraph(args.regressionId)
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

}