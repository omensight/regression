package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.secondary.LineData
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.extensions.roundedNumber
import org.apache.commons.math3.stat.regression.SimpleRegression

class LinearRegression : Regression {

    private lateinit var data: List<Pair<Double, Double>>
    private lateinit var xColumn: DoubleArray
    private lateinit var yColumn: DoubleArray
    private val simpleRegression = SimpleRegression()
    private var dataSettled = false
    override suspend fun setData(entryData: List<SheetEntry>) {
        simpleRegression.clear()
        dataSettled = true
        val sortedData = entryData.sortedBy { it.x }
        xColumn = sortedData.map { it.x?.toDouble()?:0.0 }.toDoubleArray()
        yColumn = sortedData.map { it.y?.toDouble()?:0.0 }.toDoubleArray()
        this.data = xColumn.zip(yColumn)
        data.forEach {
            simpleRegression.addData(it.first, it.second)
        }
    }

    override suspend fun getOriginalDataLine(): LineData {
        return LineData(R.string.original_data,data.sortedBy { it.first })
    }

    override suspend fun getResults(decimals: Int): List<Result> {
        require(dataSettled) { "Data not settled" }
        val result = ArrayList<Result>()
        val regressionResults = simpleRegression.regress()
        val parameterEstimates = regressionResults.parameterEstimates
        val sign = if (parameterEstimates[1] > 0) '+' else '-'

        val xAverage = xColumn.average()
        val yAverage = yColumn.average()
        val sumOfX = xColumn.sum()
        val sumOfY = yColumn.sum()
        val sumOfSquaresOfX = xColumn.map { it.times(it) }.sum()
        val sumOfSquaresOfY =  yColumn.map { it.times(it) }.sum()
        val sumOfCrossXY = data.map { it.first.times(it.second) }.sum()
        val sXX = sumOfSquaresOfX / simpleRegression.n - xAverage * xAverage
        val sYY = sumOfSquaresOfY / simpleRegression.n - yAverage * yAverage
        val sXY = sumOfCrossXY / simpleRegression.n - xAverage * yAverage

        result.add(Result(R.string.formula_fit_line_linear,
            "$$\\hat y = ${parameterEstimates[0].roundedNumber(decimals)} $sign ${parameterEstimates[1].roundedNumber(decimals)}x $$",
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

    override suspend fun getCalculatedLines(): List<LineData>{
        val dataEntries = ArrayList<Pair<Double, Double>>()
        val firstPoint = xColumn.first()
        val lastPoint = xColumn.last()
        dataEntries.add(Pair(firstPoint,simpleRegression.predict(firstPoint)))
        dataEntries.add(Pair(lastPoint,simpleRegression.predict(lastPoint)))
        val lineData = LineData(R.string.formula_fit_line, dataEntries.sortedBy { it.first })
        return listOf(lineData)
    }
}