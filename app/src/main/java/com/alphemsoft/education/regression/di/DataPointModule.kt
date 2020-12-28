package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.datasource.ISheetEntryLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.SheetEntryLocalDataSource
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
    fun providesDataPointDataSource(dataPointDao: DataPointDao): ISheetEntryLocalDataSource {
        return SheetEntryLocalDataSource(dataPointDao)
    }


}