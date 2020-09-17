package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.FragmentResultsBinding
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.ui.adapter.ResultAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.ResultViewModel
import com.google.android.gms.ads.formats.UnifiedNativeAd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class AbstractResultFragment : BaseFragment<FragmentResultsBinding, ResultViewModel>(
    layoutId = R.layout.fragment_results,
    viewModelId = BR.resultViewModel,
    menuResourceId = R.menu.menu_result
)

@AndroidEntryPoint
class ResultFragment : AbstractResultFragment() {
    override val viewModel: ResultViewModel by viewModels()
    val args: ResultFragmentArgs by navArgs()

    @Inject
    lateinit var resultAdapter: ResultAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupResultList()
        setupDataPoints()
    }

    private fun setupDataPoints() {
        coroutineHandler.foregroundScope.launch {
            refreshResultList()
        }
    }

    private suspend fun refreshResultList() {
        val unifiedNativeAds = getUnifiedNativeAds()
        if (unifiedNativeAds.isEmpty()) return
        val sheet: Sheet = viewModel.getSheet(args.regressionId)
        val regression = RegressionFactory.getRegression(sheet.type)
        val data = viewModel.getDataPoints(args.regressionId)
        regression.setData(data)

        val results = regression.getResults(10)
        val resultsWithAds: MutableList<Any> = ArrayList()
        var adIndex = 0
        results.forEachIndexed { index, result ->
            if (index % 5 == 0 && unifiedNativeAds.isNotEmpty() && index > 0) {
                val adEntity = AdEntity()
                adEntity.unifiedNativeAd = unifiedNativeAds[adIndex++]
                resultsWithAds.add(adEntity)
                if (adIndex > unifiedNativeAds.size -1){
                    adIndex = 0
                }
            }
            resultsWithAds.add(result)

        }
        resultAdapter.addNewItems(resultsWithAds)
    }

    private fun setupResultList() {
        dataBinding.rvResults.apply {
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

    override fun onAdsLoaded(unifiedNativeAds: MutableList<UnifiedNativeAd>, adsChanged: Boolean) {
        super.onAdsLoaded(unifiedNativeAds, true)
        coroutineHandler.foregroundScope.launch {
            refreshResultList()
        }
    }
}