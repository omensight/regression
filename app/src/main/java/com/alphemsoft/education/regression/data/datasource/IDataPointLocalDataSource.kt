package com.alphemsoft.education.regression.data.datasource

import androidx.paging.Pager
import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import kotlinx.coroutines.flow.Flow

interface IDataPointLocalDataSource: BaseDataSource<DataEntry, Long> {
}