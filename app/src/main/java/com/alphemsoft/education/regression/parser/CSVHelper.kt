package com.alphemsoft.education.regression.parser

import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.commons.csv.CSVParser
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.math.BigDecimal
import kotlin.math.max

class CSVHelper(private val csvParser: CSVParser) {

    private val _sheetEntries: MutableList<Pair<Double?,Double?>> = ArrayList()

    val sheetEntries: List<Pair<Double?,Double?>>
        get() = _sheetEntries

    init {
        fillDataEntries()
    }

    /**
     * Used for get entries
     */
    private fun fillDataEntries() {
        val iterator = csvParser.asSequence()
        iterator.forEach {
            _sheetEntries.add(Pair(it[0].toDoubleOrNull(),it[1].toDoubleOrNull()))
        }
    }
}