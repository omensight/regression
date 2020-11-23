package com.alphemsoft.education.regression.parser

import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.commons.csv.CSVParser
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.math.BigDecimal
import kotlin.math.max

class CSVHelper(private val csvParser: CSVParser) {

    private val _sheetEntries: MutableList<Pair<Double, Double>> = ArrayList()

    val sheetEntries: List<Pair<Double, Double>>
        get() = _sheetEntries

    var errorCount: Int = 0
        private set

    init {
        fillDataEntries()
    }

    /**
     * Used for get entries
     */
    private fun fillDataEntries() {
        val iterator = csvParser.asSequence()
        iterator.forEach {
            try {
                val pair = Pair(it[0].toDouble(), it[1].toDouble())
                _sheetEntries.add(pair)
            } catch (e: Exception) {
                errorCount++
            }
        }
    }
}