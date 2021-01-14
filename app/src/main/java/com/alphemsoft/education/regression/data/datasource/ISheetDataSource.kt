package com.alphemsoft.education.regression.data.datasource

import androidx.paging.Pager
import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.Sheet
import kotlinx.coroutines.flow.Flow

interface ISheetDataSource :
    BaseDataSource<Sheet, Long> {
    fun findAll(): Pager<Int, Sheet>
    fun getSheetItemCount(): Flow<Long>
}