package com.alphemsoft.education.regression.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alphemsoft.education.regression.data.dao.DataEntryDao
import com.alphemsoft.education.regression.data.dao.SheetColumnDao
import com.alphemsoft.education.regression.data.dao.SheetDao
import com.alphemsoft.education.regression.data.model.SheetColumn
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.roomconverter.BigDecimalConverter
import com.alphemsoft.education.regression.data.roomconverter.DateRoomConverters

@Database(entities = [Sheet::class, DataEntry::class, SheetColumn::class], version = 1)
@TypeConverters(DateRoomConverters::class, BigDecimalConverter::class)
abstract class RegressionDatabase : RoomDatabase() {
    abstract fun sheetDao(): SheetDao
    abstract fun dataPointDao(): DataEntryDao
    abstract fun sheetColumnDao(): SheetColumnDao
}