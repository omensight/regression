package com.alphemsoft.education.regression.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class GraphDecimalValueFormatter: ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val longPattern = "0E0"
        val decimalFormat = DecimalFormat(longPattern)
        return when{
            value > 100000 -> decimalFormat.format(value)
            value < -100000 -> decimalFormat.format(value)
            else -> super.getAxisLabel(value, axis)
        }
    }
}