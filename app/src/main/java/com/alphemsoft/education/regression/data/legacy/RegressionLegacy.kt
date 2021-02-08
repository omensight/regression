package com.alphemsoft.education.regression.data.legacy

import java.lang.NumberFormatException
import java.util.ArrayList

/**
 * Created by Mijael Viricochea on 12/20/2017.
 */
class RegressionLegacy(
    val id: Int,
    val type: Int,
    val name: String,
    private val xData: String,
    private val yData: String,
    val xLabel: String,
    val yLabel: String
) : Comparable<RegressionLegacy> {

    fun getDataPairs(): ArrayList<DataPair> {
        val result = ArrayList<DataPair>()
        val xValues = xData.split(";")
        val yValues = yData.split(";")
        if (xValues.size == yValues.size) {
            val zipped = xValues.zip(yValues)
            for (i in zipped.indices) {
                try {
                    val currentElement = zipped[i]
                    val element = DataPair(
                        currentElement.first.toDouble(),
                        currentElement.second.toDouble()
                    )
                    result.add(element)
                } catch (e: NumberFormatException) {
                    continue
                }
            }
        }
        return result
    }

    override fun compareTo(regressionLegacy: RegressionLegacy): Int {
        val last = name.compareTo(regressionLegacy.name)
        return if (last == 0) name.compareTo(regressionLegacy.name) else last
    }
}