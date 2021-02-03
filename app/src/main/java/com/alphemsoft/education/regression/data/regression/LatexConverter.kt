package com.alphemsoft.education.regression.data.regression

import java.text.DecimalFormat

class LatexConverter(var decimalCount: Int = 5){
    fun roundedNumber(number: Double): String{
        val format = "0.${"#".repeat(decimalCount)}E0"
        val decimalFormatter = DecimalFormat(format)
        val nonScientificPattern = "0.${"#".repeat(decimalCount)}"
        val nonScientificFormat = DecimalFormat(nonScientificPattern)
        return when{
            number >= 10 || number < 0 -> {
                decimalFormatter.format(number)
            }
            else -> nonScientificFormat.format(number)
        }
    }

    fun toLatex(number: Double): String {
        val rounded = roundedNumber(number)
        var latex = rounded.replace("E","\\times 10^")
        val indexOfExponentialSymbol = latex.indexOf('^')
        if (indexOfExponentialSymbol != -1){
            val isNegative = latex[indexOfExponentialSymbol+1] == '-'
            if (isNegative){
                latex = latex.replace("^","^{").plus('}')
            }
        }
        return latex
    }
}