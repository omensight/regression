package com.alphemsoft.education.regression.data.regression

import java.text.DecimalFormat

class LatexConverter(var decimalCount: Int = 5){
    fun roundedNumber(number: Double): String{
        val format = "0.${"#".repeat(decimalCount)}E0"
        val decimalFormatter = DecimalFormat(format)
        val nonScientificPattern = "0.${"#".repeat(decimalCount)}"
        val nonScientificFormat = DecimalFormat(nonScientificPattern)
        return when{
            number >= 10 -> {
                decimalFormatter.format(number)
            }
            else -> nonScientificFormat.format(number)
        }
    }

    fun toLatex(number: Double): String {
        val rounded = roundedNumber(number)
        return rounded.replace("E","\\times 10^")
    }
}