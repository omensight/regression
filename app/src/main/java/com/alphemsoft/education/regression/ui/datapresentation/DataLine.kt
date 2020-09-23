package com.alphemsoft.education.regression.ui.datapresentation

import androidx.annotation.StringRes
import com.github.mikephil.charting.data.Entry
import java.lang.IllegalArgumentException

data class DataLine(
    val entries: List<Entry>,
    @StringRes val lineTagRes: Int? = null,
    val lineTag: String? = null,
){
    init {
        if (lineTag == null && lineTagRes == null){
            throw IllegalArgumentException("Either lineTagRes or lineTag can be settled")
        }
    }
}