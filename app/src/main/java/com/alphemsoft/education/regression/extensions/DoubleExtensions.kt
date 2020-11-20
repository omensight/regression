package com.alphemsoft.education.regression.extensions

import java.text.DecimalFormat

fun Double.roundedNumber(decimalPositions: Int): String {
    val formatter = DecimalFormat("#.${"#".repeat(decimalPositions)}E0")
    var formattedNumber = formatter.format(this)
    if (formattedNumber.contains("E")) {
        val ePosition = formattedNumber.indexOf("E")
        val number = formattedNumber.substring(0 until ePosition)
        val exp = formattedNumber.substring(ePosition + 1)
        exp.toIntOrNull()?.let {
            formattedNumber = if (it != 0) {
                "${number} \\times 10^{${exp}}"
            } else {
                number
            }
        }
    }
    return formattedNumber
}