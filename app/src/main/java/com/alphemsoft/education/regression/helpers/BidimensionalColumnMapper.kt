package com.alphemsoft.education.regression.helpers

import com.alphemsoft.education.regression.data.model.SheetEntry

class BidimensionalColumnMapper: IColumnMapper {
    override fun getEntryColumns(entries: List<SheetEntry>): List<DoubleArray> {
        val ySize = entries.map { sheetEntry ->
            sheetEntry.yCoordinate
        }.maxOrNull()
        require(ySize != null && ySize == 1L) {"Y size not allowed, it must be 2"}
        val firstColumnEntries = entries.filter {
            it.yCoordinate == 0L
        }.map { it.data!!.toDouble() }.toDoubleArray()
        val secondColumnEntries = entries.filter {
            it.yCoordinate == 1L
        }.map { it.data!!.toDouble() }.toDoubleArray()
        return listOf(firstColumnEntries,secondColumnEntries)
    }
}