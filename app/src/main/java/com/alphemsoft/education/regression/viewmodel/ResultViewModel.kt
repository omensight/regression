package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.ISheetColumnLocalDataSource
import com.alphemsoft.education.regression.data.datasource.SheetLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.DataPointLocalDataSource
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.flow.Flow


class ResultViewModel @ViewModelInject constructor(
    private val dataPointLocalDataSource: DataPointLocalDataSource,
    private val sheetLocalDataSource: SheetLocalDataSource,
    private val sheetColumnLocalDataSource: ISheetColumnLocalDataSource
) :ViewModel() {

    fun getSheetDataColumnsFlow(sheetId: Long): Flow<List<QuerySheetDataColumn>> {
        return sheetColumnLocalDataSource.getSheetDataColumnsFlow(sheetId)
    }

    suspend fun getSheet(regressionId: Long): Sheet {
        return sheetLocalDataSource.find(regressionId)
    }

    suspend fun getDataPoints(sheetId: Long): List<QuerySheetDataColumn> {
        return sheetColumnLocalDataSource.getSheetDataColumns(sheetId)
    }
}