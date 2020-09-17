package com.alphemsoft.education.regression.extensions

import java.text.DateFormat
import java.util.*

fun Date.getSimpleDate(): String{
    val simpleDateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
    return simpleDateFormat.format(this)
}