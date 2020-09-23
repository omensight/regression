package com.alphemsoft.education.regression.data.model

import androidx.room.*
import java.math.BigDecimal
import java.util.*


@Entity(tableName = "data_entries",
foreignKeys = [ForeignKey(entity = SheetColumn::class, parentColumns = ["sheet_column_id"], childColumns = ["data_entry_fk_sheet_column_id"])])
data class DataEntry constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_entry_id")
    var id: Long = 0,
    @ColumnInfo(name = "data_entry_fk_sheet_column_id")
    val fkDataColumnId: Long,
    @ColumnInfo(name = "data_entry_row_index")
    var rowIndex: Int = -1,
    @ColumnInfo(name = "data_entry_data")
    var data: BigDecimal? = null,
    @Ignore
    var selected: Boolean = false,

    @Ignore
    var uuid: String? = null
):DbEntity(id) {
    constructor(id: Long,fkDataColumnId: Long, data: BigDecimal, rowIndex: Int) : this(id,fkDataColumnId,rowIndex, data, false, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DataEntry

        if (id != other.id) return false
        if (fkDataColumnId != other.fkDataColumnId) return false
        if (rowIndex != other.rowIndex) return false
        if (data != other.data) return false
        if (selected != other.selected) return false
        if (uuid != other.uuid) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + fkDataColumnId.hashCode()
        result = 31 * result + rowIndex
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + selected.hashCode()
        return result
    }


}