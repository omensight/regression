package com.alphemsoft.education.regression.data.model

import androidx.room.*
import java.math.BigDecimal


@Entity(
    tableName = "sheet_entries",
    primaryKeys = ["sheet_entry_id", "fk_sheet_x_coordinate", "fk_sheet_y_coordinate"],
    foreignKeys = [ForeignKey(
        entity = Sheet::class,
        parentColumns = ["sheet_id"],
        childColumns = ["fk_sheet_id"]
    )]
)
data class SheetEntry constructor(
    @ColumnInfo(name = "sheet_entry_id")
    var id: Long = 0,

    @ColumnInfo(name = "fk_sheet_x_coordinate")
    val xCoordinate: Long = 0,

    @ColumnInfo(name = "fk_sheet_y_coordinate")
    val yCoordinate: Long = 0,
    @ColumnInfo(name = "fk_sheet_id")
    val fkSheetId: Long,
    var data: BigDecimal? = null,
    @Ignore
    var selected: Boolean
) {
    constructor(id: Long, xCoordinate: Long, yCoordinate: Long, fkSheetId: Long, data: BigDecimal) : this(
        id,
        xCoordinate,
        yCoordinate,
        fkSheetId,
        data,
        false
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SheetEntry

        if (id != other.id) return false
        if (xCoordinate != other.xCoordinate) return false
        if (yCoordinate != other.yCoordinate) return false
        if (fkSheetId != other.fkSheetId) return false
        if (data != other.data) return false
        if (selected != other.selected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + xCoordinate.hashCode()
        result = 31 * result + yCoordinate.hashCode()
        result = 31 * result + fkSheetId.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + selected.hashCode()
        return result
    }

}
