package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.extensions.roundedNumber
import org.apache.commons.math3.stat.regression.SimpleRegression
import kotlin.math.absoluteValue

class LinearRegression() : Regression {

    private lateinit var data: List<DataPoint>
    private val simpleRegression = SimpleRegression()

    private var dataSettled = false
    override suspend fun setData(data: List<DataPoint>) {
        simpleRegression.clear()
        dataSettled = true
        this.data = data
        data.forEach {
            simpleRegression.addData(it.x?.toDouble() ?: 0.0, it.y?.toDouble() ?: 0.0)
        }
    }

    override suspend fun getResults(decimals: Int): List<Result> {
        require(dataSettled) { "Data not settled" }
        val result = ArrayList<Result>()
        val regressionResults = simpleRegression.regress()
        val parameterEstimates = regressionResults.parameterEstimates
        val sign = if (parameterEstimates[1] > 0) '+' else '-'
        val xAverage = data.map { it.x!!.toDouble() }.average()
        val yAverage = data.map { it.y!!.toDouble() }.average()
        val sumOfX = data.sumOf { it.x!!.toDouble() }
        val sumOfY = data.sumOf { it.y!!.toDouble() }
        val sumOfSquaresOfX = data.map { it.x?.times(it.x!!)?.toDouble()!! }.sum()
        val sumOfSquaresOfY = data.map { it.y?.times(it.y!!)?.toDouble()!! }.sum()
        val sumOfCrossXY = data.map { it.x?.times(it.y!!)?.toDouble()!! }.sum()
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

    override fun getCalculatedPoints(): List<Pair<Double, Double>> {
        val sortedData = data.sortedBy { it.x }
        val result = ArrayList<Pair<Double, Double>>()
        val firstPoint = sortedData.first().x!!.toDouble()
        val lastPoint = sortedData.last().x!!.toDouble()
        result.add(Pair(firstPoint,simpleRegression.predict(firstPoint)))
        result.add(Pair(lastPoint,simpleRegression.predict(lastPoint)))
        return result
    }
}