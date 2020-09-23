package com.alphemsoft.education.regression.data.datasource

import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.data.model.SheetColumn
import kotlinx.coroutines.flow.Flow

interface ISheetColumnLocalDataSource: BaseDataSource<SheetColumn, Long> {
    suspend fun getSheetDataColumns(sheetId: Long): List<QuerySheetDataColumn>
    fun getSheetDataColumnsFlow(sheetId: Long): Flow<List<QuerySheetDataColumn>>
}