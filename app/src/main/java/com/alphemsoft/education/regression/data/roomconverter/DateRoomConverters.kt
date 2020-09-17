package com.alphemsoft.education.regression.data.roomconverter

import androidx.room.TypeConverter
import com.alphemsoft.education.regression.data.model.SheetType
import java.util.*

class DateRoomConverters {
    @TypeConverter
    fun dateToLong(date: Date): Long{
        return date.time
    }

    @TypeConverter
    fun dateToLong(long: Long): Date{
        return Date(long)
    }

    @TypeConverter
    fun typeToInt(sheetType: SheetType?): Int?{
        return sheetType?.type
    }

    @TypeConverter
    fun intToType(int: Int?): SheetType?{
        return SheetType.values()[int?:0]
    }
}