package com.alphemsoft.education.regression.extensions

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.getSimpleDate(): String{
    val simpleDateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
    return simpleDateFormat.format(this)
}

fun Date.getSimpleFormatted(): String{
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
    return simpleDateFormat.format(this)
}