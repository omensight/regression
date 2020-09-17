package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.extensions.roundedNumber
import org.apache.commons.math3.stat.regression.SimpleRegression
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

class PowerRegression : Regression {

    private var squareOfR: Double = Double.NaN
    private var sumOfLnY: Double = Double.NaN
    private var sumOfLnX: Double = Double.NaN
    private var sXY: Double = Double.NaN
    private var sYY: Double = Double.NaN
    private var sXX: Double = Double.NaN
    private var sumOfLnXLnYProduct: Double = Double.NaN
    private var sumOfSquaresOfLnY: Double = Double.NaN
    private var sumOfSquaresOfLnX: Double = Double.NaN
    private lateinit var squaresOfLnYList: List<Double>
    private lateinit var squaresOfLnXList: List<Double>
    private var lnYAverage: Double = Double.NaN
    private var lnXAverage: Double = Double.NaN
    private var a: Double = Double.NaN
    private var b: Double = Double.NaN
    private var r: Double = Double.NaN
    private lateinit var lnXTimesLnYList: List<Double>
    private lateinit var lnYList: List<Double>
    private lateinit var lnXList: List<Double>
    private lateinit var entries: List<DataPoint>
    private val simpleRegression = SimpleRegression()
    private var dataSettled: Boolean = false

    private lateinit var result: MutableList<Result>


    override suspend fun setData(data: List<DataPoint>) {
        entries = data
        dataSettled = true
        entries.forEach {
            simpleRegression.addData(it.x!!.toDouble(), it.y!!.toDouble())
        }
        result = ArrayList()
        lnXList = entries.map { ln(it.x!!.toDouble()) }
        lnYList = entries.map { ln(it.y!!.toDouble()) }
        lnXTimesLnYList = entries.map { ln(it.x!!.toDouble()).times(ln(it.y!!.toDouble())) }
        lnXAverage = lnXList.average()
        lnYAverage = lnYList.average()
        squaresOfLnXList = lnXList.map { it.pow(2) }
        squaresOfLnYList = lnYList.map { it.pow(2) }
        sumOfSquaresOfLnX = squaresOfLnXList.sum()
        sumOfSquaresOfLnY = squaresOfLnYList.sum()
        sumOfLnXLnYProduct = lnXTimesLnYList.sum()
        sXX = sumOfSquaresOfLnX / simpleRegression.n - lnXAverage.pow(2)
        sYY = sumOfSquaresOfLnY / simpleRegression.n - lnYAverage.pow(2)
        sXY = sumOfLnXLnYProduct / simpleRegression.n - lnXAverage * lnYAverage
        sumOfLnX = lnXList.sum()
        sumOfLnY = lnYList.sum()
        b = sXY / sXX
        a = Math.E.pow(lnYAverage - b * lnXAverage)
        r = sXY / (sqrt(sXX) * sqrt(sYY))
        squareOfR = sXY / (sqrt(sXX) * sqrt(sYY))
    }

    override suspend fun getResults(decimals: Int): List<Result> {
        result.add(Result(R.string.formula_fit_line, "$$ y = Ax^B $$", sYY))

        result.add(Result(R.string.formula_fit_line,
            "$$ y = ${a.roundedNumber(decimals)}x^{${b.roundedNumber(decimals)}}$$",
            null))

        result.add(Result(R.string.a,
            "$$ A = e^{\\overline{lny}-B\\overline{lnx}} = ${a.roundedNumber(decimals)}$$",
            a))
        result.add(Result(R.string.b,
            "$$ B = \\frac{Sxy}{Sxx} = ${b.roundedNumber(decimals)}$$",
            b))
        result.add(Result(R.string.r,
            "$$ R = \\frac{Sxy}{\\sqrt{Sxx}\\sqrt{Syy}} = ${r.roundedNumber(decimals)}$$",
            r))
        result.add(Result(R.string.square_of_r,
            "$$ R^2 = ${squareOfR.roundedNumber(decimals)}",
            squareOfR))
        result.add(Result(R.string.n,
            "$$ n = ${simpleRegression.n}",
            simpleRegression.n.toDouble()))
        result.add(Result(R.string.sXX,
            "$$ Sxx = \\frac{\\sum{Lnx^2}}{n}-\\overline{Lnx}^2 = ${sXX.roundedNumber(decimals)}$$",
            sXX))
        result.add(Result(R.string.sYY,
            "$$ Syy = \\frac{\\sum{Lny^2}}{n}-\\overline{Lny}^2 = ${sYY.roundedNumber(decimals)}$$",
            sYY))
        result.add(Result(R.string.sXY,
            "$$ Sxy = \\frac{\\sum (Lnx*Lny)}{n}-\\overline{Lnx}\\overline{Lny} = ${sXY.roundedNumber(decimals)}$$",
            sXY))
        result.add(Result(R.string.ln_x_average,
            "$$\\overline{Lnx} = \\frac{\\sum {Lnx}}{n} = ${lnXAverage.roundedNumber(decimals)}$$",
            lnXAverage))
        result.add(Result(R.string.ln_y_average,
            "$$\\overline{Lny} = \\frac{\\sum {Lny}}{n} = ${lnYAverage.roundedNumber(decimals)}$$",
            lnYAverage))
        result.add(Result(R.string.sum_of_ln_x,
            "$$\\sum {Lnx} = ${sumOfLnX.roundedNumber(decimals)}$$",
            sumOfLnX))
        result.add(Result(R.string.sum_of_ln_y,
            "$$\\sum {Lny} = ${sumOfLnY.roundedNumber(decimals)}$$",
            sumOfLnY))
        result.add(Result(R.string.sum_of_ln_x_ln_y_products,
            "$$\\sum {Lnx*Lny} = ${sumOfLnXLnYProduct.roundedNumber(decimals)}$$",
            sumOfLnXLnYProduct))
        return result
    }

    override fun getCalculatedPoints(): List<Pair<Double, Double>> {
        val result = ArrayList<Pair<Double, Double>>()
        val ordered = entries.sortedBy {
            it.x
        }
        val firstPointX = ordered.first().x!!
        val lastPointX = ordered.last().x!!
        val distance = firstPointX.minus(lastPointX).toDouble().absoluteValue
        val step = distance.div(100)
        for (i in 0 .. 99) {
            step.times(i).let { currentDistance->
                val currentRealDistance = firstPointX.toDouble() + currentDistance
                result.add(Pair(currentRealDistance, a*(currentDistance.pow(b))))
            }
        }
        return result
    }

}