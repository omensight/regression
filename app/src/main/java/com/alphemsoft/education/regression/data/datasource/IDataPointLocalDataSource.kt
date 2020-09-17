package com.alphemsoft.education.regression.data.datasource

import androidx.paging.Pager
import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.DataPoint
import kotlinx.coroutines.flow.Flow

interface IDataPointLocalDataSource: BaseDataSource<DataPoint, Long> {
    fun getDataPointPager(sheetId: Long): Pager<Int, DataPoint>
    suspend fun getDataPointList(sheetId: Long): List<DataPoint>
    fun getDataPointsFlow(sheetId: Long): Flow<List<DataPoint>>
}