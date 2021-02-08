package com.alphemsoft.education.regression.data.datasource.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.datasource.ISheetEntryLocalDataSource
import com.alphemsoft.education.regression.data.model.SheetEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SheetEntryLocalDataSource @Inject constructor(
    private val dataPointDao: DataPointDao
) : ISheetEntryLocalDataSource {

    override fun getDataPointPager(sheetId: Long): Pager<Int, SheetEntry> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { dataPointDao.findSheetDataPointSource(sheetId)}
        )
    }

    override suspend fun getDataPointList(sheetId: Long): List<SheetEntry> {
        return dataPointDao.findSheetDataPoints(sheetId)
    }

    override suspend fun find(id: Long): SheetEntry? {
        return dataPointDao.findById(id)
    }

    override suspend fun insert(item: SheetEntry): Long {
        return dataPointDao.insert(item)
    }

    override suspend fun insert(items: List<SheetEntry>) {
        dataPointDao.insert(items)
    }

    override suspend fun delete(items: List<SheetEntry>) {
        dataPointDao.delete(items)
    }

    override suspend fun update(items: List<SheetEntry>) {
        dataPointDao.update(items)
    }

    override fun getDataPointsFlow(sheetId: Long): Flow<List<SheetEntry>> {
        return dataPointDao.findDataPointsFlowById(sheetId)
    }

    override suspend fun maxId(): Long {
        return dataPointDao.findMaxId()?:0
    }
}