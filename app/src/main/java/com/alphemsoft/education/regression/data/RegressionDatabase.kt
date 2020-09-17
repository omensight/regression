package com.alphemsoft.education.regression.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.dao.SheetDao
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.roomconverter.BigDecimalConverter
import com.alphemsoft.education.regression.data.roomconverter.DateRoomConverters

@Database(entities = [Sheet::class, DataPoint::class], version = 1)
@TypeConverters(DateRoomConverters::class, BigDecimalConverter::class)
abstract class RegressionDatabase : RoomDatabase() {
    abstract fun sheetDao(): SheetDao
    abstract fun dataPointDao(): DataPointDao
}