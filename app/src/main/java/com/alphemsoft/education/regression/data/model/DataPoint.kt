package com.alphemsoft.education.regression.data.model

import androidx.room.*
import java.math.BigDecimal


@Entity(tableName = "data_points",
foreignKeys = [ForeignKey(entity = Sheet::class, parentColumns = ["sheet_id"], childColumns = ["fk_sheet_id"])])
data class DataPoint constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_point_id")
    var id: Long = 0,
    @ColumnInfo(name = "fk_sheet_id")
    val fkSheetId: Long,
    var x: BigDecimal? = null,
    var y: BigDecimal? = null,
    @Ignore
    var selected: Boolean
) {
    constructor(id: Long, fkSheetId: Long,x: BigDecimal, y: BigDecimal) : this(id, fkSheetId, x, y, false)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DataPoint

        if (id != other.id) return false
        if (fkSheetId != other.fkSheetId) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (selected != other.selected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + fkSheetId.hashCode()
        result = 31 * result + (x?.hashCode() ?: 0)
        result = 31 * result + (y?.hashCode() ?: 0)
        result = 31 * result + selected.hashCode()
        return result
    }


}
