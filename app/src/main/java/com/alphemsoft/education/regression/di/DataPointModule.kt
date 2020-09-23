package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.dao.DataEntryDao
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.DataPointLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataPointModule {

    @Provides
    @Singleton
    fun providesDataPointDataSource(dataEntryDao: DataEntryDao): IDataPointLocalDataSource {
        return DataPointLocalDataSource(dataEntryDao)
    }


}