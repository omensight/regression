package com.alphemsoft.education.regression.data.regression

import java.text.DecimalFormat

class LatexConverter(var decimalCount: Int = 5){
    fun roundedNumber(number: Double): String{
        val format = "0.${"#".repeat(decimalCount)}E0"
        val scientificNotationFormat = DecimalFormat(format)
        val nonScientificPattern = "0.${"#".repeat(decimalCount)}"
        val nonScientificFormat = DecimalFormat(nonScientificPattern)
        return when{
            number >= 9 || number < 1 -> {
                scientificNotationFormat.format(number)
            }
            else -> nonScientificFormat.format(number)
        }
    }

    fun toLatex(number: Double): String {
        val rounded = roundedNumber(number)
        var latex = ""
        if (!rounded.contains("E0")){
            latex = rounded.replace("E","\\times 10^")
            val indexOfExponentialSymbol = latex.indexOf('^')
            if (indexOfExponentialSymbol != -1){
                val isNegative = latex[indexOfExponentialSymbol+1] == '-'
                if (isNegative){
                    latex = latex.replace("^","^{").plus('}')
                }
            }
        }else{
            rounded.replace("E","\\times 10^")
        }
        return latex
    }
}