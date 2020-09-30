package com.alphemsoft.education.regression.parser

import com.alphemsoft.education.regression.data.model.SheetEntry
import org.apache.commons.csv.CSVParser
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.math.BigDecimal
import kotlin.math.max

class CSVHelper(private val csvParser: CSVParser) {

    private val _sheetEntries: MutableList<Array<Double?>> = ArrayList()

    val sheetEntries: List<Array<Double?>>
        get() = _sheetEntries

    init {
        fillDataEntries()
    }

    /**
     * Used for get entries
     */
    private fun fillDataEntries() {
        val iterator = csvParser.asSequence()
        iterator.forEachIndexed { index, record ->
            val array = record.map { stringNumber ->
                stringNumber.toDoubleOrNull()
            }.toTypedArray()
            _sheetEntries.add(array)
        }
    }
}