package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.extensions.roundedNumber
import com.alphemsoft.education.regression.ui.datapresentation.DataLine
import com.github.mikephil.charting.data.Entry
import org.apache.commons.math3.stat.regression.SimpleRegression

class LinearRegression : Regression {
    private lateinit var data: List<QuerySheetDataColumn>
    private val simpleRegression = SimpleRegression()
    private lateinit var xDataEntries: List<DataEntry>
    private lateinit var yDataEntries: List<DataEntry>

    private var dataSettled = false
    override suspend fun setData(querySheetDataColumns: List<QuerySheetDataColumn>) {
        require(querySheetDataColumns.size == 2) { "Required a two size list" }
        xDataEntries = querySheetDataColumns[0].dataEntries
        yDataEntries = querySheetDataColumns[1].dataEntries
        require(xDataEntries.size == yDataEntries.size) { "The entries list have not the same size" }
        simpleRegression.clear()
        this.data = querySheetDataColumns
        for (i in xDataEntries.indices){
            simpleRegression.addData(xDataEntries[i].data?.toDouble() ?: 0.0, yDataEntries[i].data?.toDouble() ?: 0.0)
        }
        dataSettled = true
    }

    override suspend fun getResults(decimals: Int): List<Result> {
        require(dataSettled) { "Data not settled" }
        val result = ArrayList<Result>()
        val regressionResults = simpleRegression.regress()
        val parameterEstimates = regressionResults.parameterEstimates
        val sign = if (parameterEstimates[1] > 0) '+' else '-'
        val xColumn = xDataEntries.map{
            it.data!!.toDouble()
        }

        val yColumn = yDataEntries.map {
            it.data!!.toDouble()
        }

        val xAverage = xColumn.average()
        val yAverage = yColumn.average()
        val sumOfX = xColumn.sum()
        val sumOfY = yColumn.sum()
        val sumOfSquaresOfX = xColumn.map { it.times(it) }.sum()
        val sumOfSquaresOfY = yColumn.map { it.times(it) }.sum()
        val sumOfCrossXY = xColumn.mapIndexed { i, x ->
            x.times(yColumn[i])
        }.sum()
        val sXX = sumOfSquaresOfX / simpleRegression.n - xAverage * xAverage
        val sYY = sumOfSquaresOfY / simpleRegression.n - yAverage * yAverage
        val sXY = sumOfCrossXY / simpleRegression.n - xAverage * yAverage

        result.add(Result(R.string.formula_fit_line_linear,
            "$$\\hat y = ${parameterEstimates[0]} $sign ${parameterEstimates[1]}x $$",
            null))
        result.add(Result(R.string.a,
            "$$ A = \\bar {y} - B \\bar x = ${parameterEstimates[0].roundedNumber(decimals)}$$",
            parameterEstimates[0]))

        result.add(Result(R.string.b,
            "$$ B = \\frac{Sxy}{Sxx} = ${parameterEstimates[1].roundedNumber(decimals)}$$",
            parameterEstimates[1]))

        val r = simpleRegression.r
        result.add(Result(R.string.r,
            "$$ R = \\frac{Sxy}{\\sqrt{Sxx} \\sqrt{Syy}} = ${r.roundedNumber(decimals)}$$",
            r))

        result.add(Result(R.string.square_of_r,
            "$$ R^2 = ${(r * r).roundedNumber(decimals)} $$",
            r * r))

        result.add(Result(R.string.n,
            "$$ n = ${simpleRegression.n.toDouble()} $$",
            simpleRegression.n.toDouble()))
        result.add(Result(R.string.sXX,
            "$$ Sxx = \\frac {\\sum x^2}{n} - \\bar{x}^2 = ${sXX.roundedNumber(decimals)}$$",
            sXX))
        result.add(Result(R.string.sYY,
            "$$ Syy = \\frac {\\sum y^2}{n} - \\bar{y}^2 = ${sYY.roundedNumber(decimals)} $$", sYY))
        result.add(Result(R.string.sXY,
            "$$ Syy = \\frac {\\sum y^2}{n} - \\bar{y}^2 = ${sXY.roundedNumber(decimals)} $$",
            sXY))
        result.add(Result(R.string.x_average,
            "$$\\bar x = ${xAverage.roundedNumber(decimals)}$$",
            xAverage))
        result.add(Result(R.string.y_average,
            "$$\\bar y = ${yAverage.roundedNumber(decimals)}$$",
            yAverage))
        result.add(Result(R.string.sum_of_x,
            "$$\\sum x = ${sumOfX.roundedNumber(decimals)}$$",
            sumOfX))
        result.add(Result(R.string.sum_of_y,
            "$$\\sum x = ${sumOfY.roundedNumber(decimals)}$$",
            sumOfY))
        result.add(Result(R.string.sum_of_xy_products,
            "$$ \\sum (x*y) = ${sumOfCrossXY.roundedNumber(decimals)}$$",
            sumOfCrossXY))
        result.add(Result(R.string.sum_of_squares_of_x,
            "$$ \\sum {X^2} = ${sumOfSquaresOfX.roundedNumber(decimals)}$$",
            sumOfSquaresOfX))
        result.add(Result(R.string.sum_of_squares_of_y,
            "$$ \\sum {Y^2} = ${sumOfSquaresOfY.roundedNumber(decimals)}$$",
            sumOfSquaresOfY))
        return result
    }

    override suspend fun getGraphLines(): List<DataLine> {
        val sortedData = xDataEntries.mapIndexed { i,x->
            val y = yDataEntries[i].data!!
            Entry(x.data!!.toFloat(), y.toFloat())
        }.sortedBy { it.x }
        val fitLine = ArrayList<Entry>()
        val originalDataLine = DataLine(sortedData, R.string.original_data)
        val firstPoint = sortedData.first().x.toDouble()
        val lastPoint = sortedData.last().x.toDouble()
        fitLine.add(Entry(firstPoint.toFloat(),simpleRegression.predict(firstPoint).toFloat()))
        fitLine.add(Entry(lastPoint.toFloat(),simpleRegression.predict(lastPoint).toFloat()))
        val fitDataLine = DataLine(fitLine, R.string.formula_fit_line)
        return listOf(originalDataLine,fitDataLine)
    }
}