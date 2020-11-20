package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.secondary.LineData
import com.alphemsoft.education.regression.data.model.secondary.Result

interface Regression {
    suspend fun getResults(decimals: Int): List<Result>
    suspend fun setData(entryData: List<SheetEntry>)
    suspend fun getOriginalDataLine(): LineData
    suspend fun getCalculatedLines(): List<LineData>
}