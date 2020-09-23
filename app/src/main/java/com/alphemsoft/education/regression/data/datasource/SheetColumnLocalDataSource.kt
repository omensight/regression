package com.alphemsoft.education.regression.data.datasource

import com.alphemsoft.education.regression.data.dao.SheetColumnDao
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.SheetColumn
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SheetColumnLocalDataSource @Inject constructor(
    private val sheetColumnDao: SheetColumnDao
): ISheetColumnLocalDataSource {
    override suspend fun getSheetDataColumns(sheetId: Long): List<QuerySheetDataColumn> {
        return sheetColumnDao.findSheetColumns(sheetId)
    }

    override suspend fun find(id: Long): SheetColumn {
        return sheetColumnDao.findById(id)
    }

    override suspend fun insert(item: SheetColumn): Long {
        return sheetColumnDao.insert(item)
    }

    override suspend fun insert(items: List<SheetColumn>) {
        sheetColumnDao.insert(items)
    }

    override suspend fun delete(items: List<SheetColumn>) {
        sheetColumnDao.delete(items)
    }

    override suspend fun update(items: List<SheetColumn>) {
        sheetColumnDao.update(items)
    }

    override fun getSheetDataColumnsFlow(sheetId: Long): Flow<List<QuerySheetDataColumn>> {
        return sheetColumnDao.findSheetColumnsFlow(sheetId)
    }
}
