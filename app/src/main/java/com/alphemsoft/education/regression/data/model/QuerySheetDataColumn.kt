package com.alphemsoft.education.regression.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class QuerySheetDataColumn(
    @Embedded
    var sheetColumn: SheetColumn,

    @Relation(parentColumn = "sheet_column_id", entityColumn = "data_entry_fk_sheet_column_id")
    var dataEntries: List<DataEntry>)
{

    override fun equals(other: Any?): Boolean{
        if (javaClass != other?.javaClass) return false

        other as QuerySheetDataColumn

        if (sheetColumn != other.sheetColumn) return false
        if (dataEntries != other.dataEntries) return false
        val zip = dataEntries.zip(other.dataEntries)
        val areItemsTheSame = zip.all {
            it.first == it.second
        }
        if (!areItemsTheSame) return false
        return true
    }

    override fun hashCode(): Int {
        var result = sheetColumn.hashCode()
        result = 31 * result + dataEntries.hashCode()
        return result
    }


}