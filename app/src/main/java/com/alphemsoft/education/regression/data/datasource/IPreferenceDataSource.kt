package com.alphemsoft.education.regression.data.datasource

import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.PreferenceModel
import kotlinx.coroutines.flow.Flow

interface IPreferenceDataSource: BaseDataSource<PreferenceModel, Long>{
    fun findUniqueDataStoreFlow(): Flow<PreferenceModel>
}