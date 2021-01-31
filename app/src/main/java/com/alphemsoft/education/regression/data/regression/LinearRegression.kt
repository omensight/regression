package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.secondary.LineData
import com.alphemsoft.education.regression.data.model.secondary.Result
import org.apache.commons.math3.stat.regression.SimpleRegression

class LinearRegression : Regression {

    private lateinit var data: List<Pair<Double, Double>>
    private lateinit var xColumn: DoubleArray
    private lateinit var yColumn: DoubleArray
    private val simpleRegression = SimpleRegression()
    private var dataSettled = false
    private val latexConverter: LatexConverter = LatexConverter()

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
        latexConverter.decimalCount = decimals
        val result = ArrayList<Result>()
        val regressionResults = simpleRegression.regress()
        val parameterEstimates = regressionResults.parameterEstimates

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
        val sign = if (parameterEstimates[1] < 0) "" else "+"
        result.add(Result(R.string.formula_fit_line_linear,
            "$$\\hat y = ${latexConverter.toLatex(parameterEstimates[0])} $sign ${latexConverter.toLatex(parameterEstimates[1])}x $$",
            null))
        result.add(Result(R.string.a,
            "$$ A = \\bar {y} - B \\bar x = ${latexConverter.toLatex(parameterEstimates[0])}$$",
            parameterEstimates[0]))

        result.add(Result(R.string.b,
            "$$ B = \\frac{Sxy}{Sxx} = ${latexConverter.toLatex(parameterEstimates[1])}$$",
            parameterEstimates[1]))

        val r = simpleRegression.r
        result.add(Result(R.string.r,
            "$$ R = \\frac{Sxy}{\\sqrt{Sxx} \\sqrt{Syy}} = ${latexConverter.toLatex(r)}$$",
            r))

        result.add(Result(R.string.square_of_r,
            "$$ R^2 = ${latexConverter.toLatex(r * r)} $$",
            r * r))

        result.add(Result(R.string.n,
            "$$ n = ${simpleRegression.n.toDouble()} $$",
            simpleRegression.n.toDouble()))
        result.add(Result(R.string.sXX,
            "$$ Sxx = \\frac {\\sum x^2}{n} - \\bar{x}^2 = ${latexConverter.toLatex(sXX)}$$",
            sXX))
        result.add(Result(R.string.sYY,
            "$$ Syy = \\frac {\\sum y^2}{n} - \\bar{y}^2 = ${latexConverter.toLatex(sYY)} $$", sYY))
        result.add(Result(R.string.sXY,
            "$$ Syy = \\frac {\\sum y^2}{n} - \\bar{y}^2 = ${latexConverter.toLatex(sXY)} $$",
            sXY))
        result.add(Result(R.string.x_average,
            "$$\\bar x = ${latexConverter.toLatex(xAverage)}$$",
            xAverage))
        result.add(Result(R.string.y_average,
            "$$\\bar y = ${latexConverter.toLatex(yAverage)}$$",
            yAverage))
        result.add(Result(R.string.sum_of_x,
            "$$\\sum x = ${latexConverter.toLatex(sumOfX)}$$",
            sumOfX))
        result.add(Result(R.string.sum_of_y,
            "$$\\sum x = ${latexConverter.toLatex(sumOfY)}$$",
            sumOfY))
        result.add(Result(R.string.sum_of_xy_products,
            "$$ \\sum (x*y) = ${latexConverter.toLatex(sumOfCrossXY)}$$",
            sumOfCrossXY))
        result.add(Result(R.string.sum_of_squares_of_x,
            "$$ \\sum {X^2} = ${latexConverter.toLatex(sumOfSquaresOfX)}$$",
            sumOfSquaresOfX))
        result.add(Result(R.string.sum_of_squares_of_y,
            "$$ \\sum {Y^2} = ${latexConverter.toLatex(sumOfSquaresOfY)}$$",
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