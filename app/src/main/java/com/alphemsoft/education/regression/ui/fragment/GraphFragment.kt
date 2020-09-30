package com.alphemsoft.education.regression.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.databinding.FragmentGraphBinding
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.ResultViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class AbstractGraphFragment() : BaseFragment<FragmentGraphBinding, ResultViewModel>(
    layoutId = R.layout.fragment_graph,
    viewModelId = BR.graph_view_model
)

@AndroidEntryPoint
class GraphFragment : AbstractGraphFragment() {
    override val viewModel: ResultViewModel by viewModels()
    val args: GraphFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupGraphics()
    }

    private fun setupGraphics() {
        coroutineHandler.foregroundScope.launch {
            viewModel.getDataPointsFlow(args.sheetId).collectLatest { originalDataPoints ->
                val originalEntries = originalDataPoints.map { dataPoint ->
                    Entry(dataPoint.data!!.toFloat(), dataPoint.y!!.toFloat())
                }.sortedBy { it.x }
                val sheet = viewModel.getSheet(args.sheetId)
                val regression = RegressionFactory.getRegression(sheet.type)
                regression.setData(originalDataPoints)
                val calculatedPoints: List<Pair<Double, Double>> = regression.getCalculatedPoints()
                val calculatedEntries = calculatedPoints.map { pair ->
                    Entry(pair.first.toFloat(), pair.second.toFloat())
                }
                val originalLineDataSet =
                    LineDataSet(originalEntries, getString(R.string.original_data))
                val calculatedDataSet = LineDataSet(calculatedEntries, getString(R.string.formula_fit_line))
                originalLineDataSet.apply {
                    setCircleColor(ContextCompat.getColor(requireContext(),
                        R.color.color_original_data))
                    color = ContextCompat.getColor(requireContext(),
                        R.color.color_original_data)
                }
                calculatedDataSet.apply {
                    color = ContextCompat.getColor(requireContext(),
                        R.color.color_calculated_data)
                    setCircleColor(Color.TRANSPARENT)
                    circleHoleColor = Color.TRANSPARENT
                }
                val lineData = LineData(originalLineDataSet, calculatedDataSet)
                dataBinding.lineChartGraph.data = lineData
                dataBinding.lineChartGraph.invalidate()
            }
        }
    }
}