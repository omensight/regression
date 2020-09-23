package com.alphemsoft.education.regression.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sheet_columns",
    foreignKeys = [ForeignKey(entity = Sheet::class, parentColumns = ["sheet_id"], childColumns = ["sheet_column_fk_sheet_id"])])
data class SheetColumn(
    @ColumnInfo(name = "sheet_column_id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "sheet_column_fk_sheet_id")
    val fkSheetId: Long,
    @ColumnInfo(name = "sheet_column_index")
    val columnIndex: Long = -1,
) : DbEntity(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as SheetColumn

        if (id != other.id) return false
        if (fkSheetId != other.fkSheetId) return false
        if (columnIndex != other.columnIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + fkSheetId.hashCode()
        result = 31 * result + columnIndex.hashCode()
        return result
    }
}