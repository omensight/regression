package com.alphemsoft.education.regression.data.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.alphemsoft.education.regression.data.dao.SheetDao
import com.alphemsoft.education.regression.data.model.Sheet
import javax.inject.Inject

class SheetLocalDataSource @Inject constructor(private val sheetDao: SheetDao) : ISheetDataSource {
    override fun findAll(): Pager<Int, Sheet> {
        return Pager(
            config = PagingConfig(10, 30, true),
            pagingSourceFactory = { sheetDao.findAll() }
        )
    }

    override suspend fun find(id: Long): Sheet {
        return sheetDao.findSheetById(id)
    }

    override suspend fun insert(item: Sheet): Long {
        return sheetDao.insert(item)
    }

    override suspend fun delete(items: List<Sheet>) {
        sheetDao.delete(items)
    }

    override suspend fun update(items: List<Sheet>) {
        sheetDao.update(items)
    }

    override suspend fun insert(items: List<Sheet>) {
        sheetDao.insert(items)
    }
}