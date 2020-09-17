package com.alphemsoft.education.regression.data.datasource.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.model.DataPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataPointLocalDataSource @Inject constructor(
    private val dataPointDao: DataPointDao
) : IDataPointLocalDataSource {

    override fun getDataPointPager(sheetId: Long): Pager<Int, DataPoint> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { dataPointDao.findSheetDataPointSource(sheetId)}
        )
    }

    override suspend fun getDataPointList(sheetId: Long): List<DataPoint> {
        return dataPointDao.findSheetDataPoints(sheetId)
    }

    override suspend fun find(id: Long): DataPoint {
        return dataPointDao.findById(id)
    }

    override suspend fun insert(item: DataPoint): Long {
        return dataPointDao.insert(item)
    }

    override suspend fun insert(items: List<DataPoint>) {
        dataPointDao.insert(items)
    }

    override suspend fun delete(items: List<DataPoint>) {
        dataPointDao.delete(items)
    }

    override suspend fun update(items: List<DataPoint>) {
        dataPointDao.update(items)
    }

    override fun getDataPointsFlow(sheetId: Long): Flow<List<DataPoint>> {
        return dataPointDao.findDataPointsFlowById(sheetId)
    }
}