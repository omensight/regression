package com.alphemsoft.education.regression.data.datasource.base

import com.alphemsoft.education.regression.data.dao.DataEntryDao
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.model.DataEntry
import javax.inject.Inject

class DataPointLocalDataSource @Inject constructor(
    private val dataEntryDao: DataEntryDao
) : IDataPointLocalDataSource {


    override suspend fun find(id: Long): DataEntry {
        return dataEntryDao.findById(id)
    }

    override suspend fun insert(item: DataEntry): Long {
        return dataEntryDao.insert(item)
    }

    override suspend fun insert(items: List<DataEntry>) {
        dataEntryDao.insert(items)
    }

    override suspend fun delete(items: List<DataEntry>) {
        dataEntryDao.delete(items)
    }

    override suspend fun update(items: List<DataEntry>) {
        dataEntryDao.update(items)
    }
}