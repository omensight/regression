package com.alphemsoft.education.regression.data.datasource.base

import com.alphemsoft.education.regression.data.dao.PreferenceDao
import com.alphemsoft.education.regression.data.datasource.IPreferenceDataSource
import com.alphemsoft.education.regression.data.model.PreferenceModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(private val preferenceDao: PreferenceDao): IPreferenceDataSource {
    override fun findUniqueDataStoreFlow(): Flow<PreferenceModel> {
        return preferenceDao.findUniquePreferenceFlow()
    }

    override suspend fun find(id: Long): PreferenceModel? {
        return preferenceDao.findUniquePreference()
    }

    override suspend fun insert(item: PreferenceModel): Long {
        return preferenceDao.insert(item)
    }

    override suspend fun insert(items: List<PreferenceModel>) {
        preferenceDao.insert(items)
    }

    override suspend fun delete(items: List<PreferenceModel>) {
        preferenceDao.delete(items)
    }

    override suspend fun update(items: List<PreferenceModel>) {
        preferenceDao.update(items)
    }
}