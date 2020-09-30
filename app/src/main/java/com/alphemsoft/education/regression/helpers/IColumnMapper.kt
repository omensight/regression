package com.alphemsoft.education.regression.helpers

import com.alphemsoft.education.regression.data.model.SheetEntry

interface IColumnMapper {
    fun getEntryColumns(entries: List<SheetEntry>): List<DoubleArray>
}