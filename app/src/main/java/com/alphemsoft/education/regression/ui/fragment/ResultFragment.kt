package com.alphemsoft.education.regression.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.FragmentResultsBinding
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.ui.adapter.ResultAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerSpacingItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class AbstractResultFragment : BaseFragment<FragmentResultsBinding, ResultViewModel>(
    layoutId = R.layout.fragment_results,
    viewModelId = BR.resultViewModel,
    menuResourceId = R.menu.menu_result
)

@AndroidEntryPoint
class ResultFragment : AbstractResultFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override val viewModel: ResultViewModel by activityViewModels()
    val args: ResultFragmentArgs by navArgs()

    private val resultAdapter: ResultAdapter = ResultAdapter()
    private lateinit var preferences: SharedPreferences
    private var started = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupResultList()
        setupPreferences()
        if (!started){
            coroutineHandler.backgroundScope.launch {
                refreshResultList()
            }
            started = true
        }
    }

    override fun onResume() {
        super.onResume()
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun setupPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
    }

    private suspend fun refreshResultList() {

        viewModel.getDataPointsFlow(args.regressionId).collectLatest {data->
            var unifiedNativeNativeAds: List<NativeAdEntity> = nativeAdDispatcher.fetchAds()
            loadResults(data, unifiedNativeNativeAds)
            if (unifiedNativeNativeAds.isEmpty()){
                delay(10000)
                unifiedNativeNativeAds = nativeAdDispatcher.fetchAds()
                loadResults(data, unifiedNativeNativeAds)
            }
        }
    }

    private suspend fun loadResults(
        data: List<SheetEntry>,
        unifiedNativeNativeAds: List<NativeAdEntity>
    ) {
        val decimalRoundCount = preferences.getInt(
            getString(R.string.key_preference_decimal_count),
            resources.getInteger(R.integer.default_decimal_count)
        )
        val sheet = viewModel.getSheet(args.regressionId)
        sheet?.let {
            val regression = RegressionFactory.generateRegression(sheet.type)
            regression.setData(data)
            val results = regression.getResults(decimalRoundCount)
            val resultsWithAds: MutableList<Any> = ArrayList()
            var adIndex = 0
            results.forEachIndexed { index, result ->
                if (index % 5 == 0 && index > 0) {
                    val adEntity = NativeAdEntity()
                    if (unifiedNativeNativeAds.isNotEmpty()) {
                        adEntity.unifiedNativeAd = unifiedNativeNativeAds[adIndex++].unifiedNativeAd
                        if (adIndex > unifiedNativeNativeAds.size - 1) {
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

    private fun setupResultList() {

        dataBinding.rvResults.apply {
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = resultAdapter
            addItemDecoration(DividerSpacingItemDecoration(16))
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d("PrefChanged","Changed")
        if (key == getString(R.string.key_preference_decimal_count)){
            coroutineHandler.backgroundScope.launch {
                val ads = nativeAdDispatcher.fetchAds()
                loadResults(viewModel.getDataPoints(args.regressionId), ads)
            }
        }
    }

}