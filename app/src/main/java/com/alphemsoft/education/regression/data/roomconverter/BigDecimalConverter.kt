package com.alphemsoft.education.regression.data.roomconverter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter{
    @TypeConverter
    fun convertBigDecimalToString(bigDecimal: BigDecimal): String{
        return bigDecimal.toString()
    }

    @TypeConverter
    fun convertStringToBigDecimal(decimalString: String): BigDecimal{
        return BigDecimal(decimalString)
    }
}