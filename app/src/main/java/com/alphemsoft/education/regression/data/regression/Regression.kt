package com.alphemsoft.education.regression.data.regression

import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.ui.datapresentation.DataLine

interface Regression {
    suspend fun setData(querySheetDataColumns: List<QuerySheetDataColumn>)
    suspend fun getResults(decimals: Int): List<Result>
    suspend fun getGraphLines(): List<DataLine>
}