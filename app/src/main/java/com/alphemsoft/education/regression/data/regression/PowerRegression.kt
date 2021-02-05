package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.secondary.LineData
import com.alphemsoft.education.regression.data.model.secondary.Result
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
    private val simpleRegression = SimpleRegression()
    private var dataSettled: Boolean = false
    private lateinit var xColumn: DoubleArray
    private lateinit var yColumn: DoubleArray
    private var n: Int = 0
    private lateinit var result: MutableList<Result>
    private val latexConverter: LatexConverter = LatexConverter()

    override suspend fun setData(entryData: List<SheetEntry>) {
        dataSettled = true
        n = entryData.size
        xColumn = entryData.map { it.x!!.toDouble() }.toDoubleArray()
        yColumn = entryData.map { it.y!!.toDouble() }.toDoubleArray()
        result = ArrayList()
        lnXList = xColumn.map { ln(it) }
        lnYList = yColumn.map { ln(it) }
        lnXTimesLnYList = lnXList.zip(lnYList).map { it.first*it.second }
        lnXAverage = lnXList.average()
        lnYAverage = lnYList.average()
        squaresOfLnXList = lnXList.map { it*it }
        squaresOfLnYList = lnYList.map { it*it }
        sumOfSquaresOfLnX = squaresOfLnXList.sum()
        sumOfSquaresOfLnY = squaresOfLnYList.sum()
        sumOfLnXLnYProduct = lnXTimesLnYList.sum()
        sXX = (sumOfSquaresOfLnX / n) - lnXAverage.pow(2)
        sYY = sumOfSquaresOfLnY / n - lnYAverage.pow(2)
        sXY = sumOfLnXLnYProduct / n - lnXAverage * lnYAverage
        sumOfLnX = lnXList.sum()
        sumOfLnY = lnYList.sum()
        b = sXY / sXX
        a = Math.E.pow(lnYAverage - b * lnXAverage)
        r = sXY / (sqrt(sXX) * sqrt(sYY))
        squareOfR = sXY / (sqrt(sXX) * sqrt(sYY))
    }

    override suspend fun getResults(decimals: Int): List<Result> {
        latexConverter.decimalCount = decimals
        result.add(Result(R.string.formula_fit_line, "$$ y = Ax^B $$", sYY))

        result.add(
            Result(
                R.string.formula_fit_line,
                "$$ y = ${latexConverter.toLatex(a)}x^{${latexConverter.toLatex(b)}}$$",
                null
            )
        )

        result.add(
            Result(
                R.string.a,
                "$$ A = e^{\\overline{lny}-B\\overline{lnx}} = ${latexConverter.toLatex(a)}$$",
                a
            )
        )
        result.add(
            Result(
                R.string.b,
                "$$ B = \\frac{Sxy}{Sxx} = ${latexConverter.toLatex(a)}$$",
                b
            )
        )
        result.add(
            Result(
                R.string.r,
                "$$ R = \\frac{Sxy}{\\sqrt{Sxx}\\sqrt{Syy}} = ${latexConverter.toLatex(r)}$$",
                r
            )
        )
        result.add(
            Result(
                R.string.square_of_r,
                "$$ R^2 = ${latexConverter.toLatex(squareOfR)}",
                squareOfR
            )
        )
        result.add(
            Result(
                R.string.n,
                "$$ n = $n",
                simpleRegression.n.toDouble()
            )
        )
        result.add(
            Result(
                R.string.sXX,
                "$$ Sxx = \\frac{\\sum{Lnx^2}}{n}-\\overline{Lnx}^2 = ${latexConverter.toLatex(sXX)}$$",
                sXX
            )
        )
        result.add(
            Result(
                R.string.sYY,
                "$$ Syy = \\frac{\\sum{Lny^2}}{n}-\\overline{Lny}^2 = ${latexConverter.toLatex(sYY)}$$",
                sYY
            )
        )
        result.add(
            Result(
                R.string.sXY,
                "$$ Sxy = \\frac{\\sum (Lnx*Lny)}{n}-\\overline{Lnx}\\overline{Lny} = ${
                    latexConverter.toLatex(sXY)
                }$$",
                sXY
            )
        )
        result.add(
            Result(
                R.string.ln_x_average,
                "$$\\overline{Lnx} = \\frac{\\sum {Lnx}}{n} = ${latexConverter.toLatex(lnXAverage)}$$",
                lnXAverage
            )
        )
        result.add(
            Result(
                R.string.ln_y_average,
                "$$\\overline{Lny} = \\frac{\\sum {Lny}}{n} = ${latexConverter.toLatex(lnYAverage)}$$",
                lnYAverage
            )
        )
        result.add(
            Result(
                R.string.sum_of_ln_x,
                "$$\\sum {Lnx} = ${latexConverter.toLatex(sumOfLnX)}$$",
                sumOfLnX
            )
        )
        result.add(
            Result(
                R.string.sum_of_ln_y,
                "$$\\sum {Lny} = ${latexConverter.toLatex(sumOfLnY)}$$",
                sumOfLnY
            )
        )
        result.add(
            Result(
                R.string.sum_of_ln_x_ln_y_products,
                "$$\\sum {Lnx*Lny} = ${latexConverter.toLatex(sumOfLnXLnYProduct)}$$",
                sumOfLnXLnYProduct
            )
        )
        return result
    }

    override suspend fun getCalculatedLines(): List<LineData> {
        val calculated = ArrayList<Pair<Double, Double>>()
        val result = ArrayList<LineData>()
        val ordered = xColumn.zip(yColumn).sortedBy {
            it.first
        }
        val firstPointX = ordered.first().first
        val lastPointX = ordered.last().first
        val distance = firstPointX.minus(lastPointX).absoluteValue
        val stepCount = 50
        val step = distance.div(stepCount)
        for (i in 0 ..stepCount) {
            step.times(i).let { currentDistance->
                val x = firstPointX + currentDistance
                var y = a * x.pow(b)
                if (y.isInfinite()){
                    y = ordered.map { it.second }.maxOrNull()!!
                }
                calculated.add(Pair(x, y))
            }
        }
        result.add(LineData(R.string.formula_fit_line, calculated))
        return result
    }

    override suspend fun getOriginalDataLine(): LineData {
        val originalData = xColumn.zip(yColumn)
        return LineData(R.string.formula_fit_line, originalData.sortedBy { it.first })
    }
}
