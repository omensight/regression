package com.alphemsoft.education.regression.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sheets")
data class Sheet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sheet_id")
    val id: Long = 0,
    @ColumnInfo(name = "type")
    val type: SheetType = SheetType.LINEAR,
    var name: String,
    @ColumnInfo(name = "x_label")
    var xLabel: String = "",
    @ColumnInfo(name = "y_label")
    var yLabel: String = "",
    @ColumnInfo(name = "created_on")
    val createdOn: Date
)