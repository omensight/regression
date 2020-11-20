package com.alphemsoft.education.regression.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetType
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.databinding.FragmentGraphBinding
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.ResultViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
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
        setBasicGraphicalUI()
        setupGraphics()
    }

    private fun setupGraphics() {
        coroutineHandler.foregroundScope.launch {
            viewModel.getDataPointsFlow(args.sheetId).collectLatest { originalDataPoints ->
                val sheet = viewModel.getSheet(args.sheetId)
                val regression = RegressionFactory.generateRegression(sheet?.type!!)
                regression.setData(originalDataPoints)
                val originalEntries = regression.getOriginalDataLine().dataPoints.map {
                    Entry(it.first.toFloat(), it.second.toFloat())
                }
                val originalLineDataSet =
                    LineDataSet(originalEntries, getString(R.string.original_data))

                val calculatedEntries = regression.getCalculatedLines()[0].dataPoints.map {
                    Entry(it.first.toFloat(), it.second.toFloat())
                }
                val calculatedDataSet =
                    LineDataSet(calculatedEntries, getString(R.string.formula_fit_line))
                originalLineDataSet.apply {
                    setCircleColor(ContextCompat.getColor(requireContext(), R.color.color_original_data))
                    valueTextColor = Color.TRANSPARENT
                    color = Color.TRANSPARENT
                    lineWidth = 0f
                    formLineWidth = 0f
                    highlightLineWidth = 0f
                }
                calculatedDataSet.apply {
                    color = ContextCompat.getColor(requireContext(), R.color.color_calculated_data)
                    setDrawCircleHole(false)
                    circleHoleColor = Color.TRANSPARENT
                    setCircleColor(Color.TRANSPARENT)
                    valueTextColor = Color.TRANSPARENT
                    mode = LineDataSet.Mode.CUBIC_BEZIER

                }
                val lineData = LineData()

                lineData.addDataSet(originalLineDataSet)
                lineData.addDataSet(calculatedDataSet)
                dataBinding.lineChartGraph.data = lineData
                dataBinding.lineChartGraph.invalidate()

            }
        }
    }

    private fun setBasicGraphicalUI(){
        val dataLegendEntry = LegendEntry().apply {
            form = Legend.LegendForm.CIRCLE
            formColor = ContextCompat.getColor(requireContext(), R.color.color_original_data)
            label = getString(R.string.original_data)
        }
        val fitLineLegendEntry = LegendEntry().apply {
            form = Legend.LegendForm.LINE
            formColor = ContextCompat.getColor(requireContext(), R.color.color_calculated_data)
            label = getString(R.string.formula_fit_line)
        }

        val legendEntries = ArrayList<LegendEntry>()

        legendEntries.add(dataLegendEntry)
        legendEntries.add(fitLineLegendEntry)
        dataBinding.lineChartGraph.legend.setCustom(legendEntries)
        val xAxis = dataBinding.lineChartGraph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        legendEntries.add(dataLegendEntry)
        legendEntries.add(fitLineLegendEntry)
    }
}