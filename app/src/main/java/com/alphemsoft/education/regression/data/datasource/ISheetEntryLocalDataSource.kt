package com.alphemsoft.education.regression.data.datasource

import androidx.paging.Pager
import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.SheetEntry
import kotlinx.coroutines.flow.Flow

interface ISheetEntryLocalDataSource: BaseDataSource<SheetEntry, Long> {
    fun getDataPointPager(sheetId: Long): Pager<Int, SheetEntry>
    suspend fun getDataPointList(sheetId: Long): List<SheetEntry>
    fun getDataPointsFlow(sheetId: Long): Flow<List<SheetEntry>>
}