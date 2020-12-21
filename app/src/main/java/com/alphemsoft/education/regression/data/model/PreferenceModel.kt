package com.alphemsoft.education.regression.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class PreferenceModel(
    @ColumnInfo(name = "preference_decimal_rounding_count")
    var decimalRoundingCount: Int,
){
    @PrimaryKey
    @ColumnInfo(name = "preference_id")
    var id: Int = 0
}