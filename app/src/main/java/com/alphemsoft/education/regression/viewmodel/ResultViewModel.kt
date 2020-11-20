package com.alphemsoft.education.regression.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.alphemsoft.education.regression.data.datasource.SheetLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.DataPointLocalDataSource
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.flow.Flow


class ResultViewModel @ViewModelInject constructor(
    private val dataPointLocalDataSource: DataPointLocalDataSource,
    private val sheetLocalDataSource: SheetLocalDataSource,
) :ViewModel() {

    fun getDataPointsFlow(sheetId: Long): Flow<List<SheetEntry>> {
        return dataPointLocalDataSource.getDataPointsFlow(sheetId)
    }

    suspend fun getSheet(regressionId: Long): Sheet? {
        return sheetLocalDataSource.find(regressionId)
    }

    suspend fun getDataPoints(sheetId: Long): List<SheetEntry> {
        return dataPointLocalDataSource.getDataPointList(sheetId)
    }
}