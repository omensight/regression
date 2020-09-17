package com.alphemsoft.education.regression.module

import android.content.Context
import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.datasource.IDataPointLocalDataSource
import com.alphemsoft.education.regression.data.datasource.base.DataPointLocalDataSource
import com.alphemsoft.education.regression.ui.adapter.DataPointAdapter
import com.alphemsoft.education.regression.ui.adapter.ResultAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataPointModule {

    @Provides
    @Singleton
    fun providesDataPointDataSource(dataPointDao: DataPointDao): IDataPointLocalDataSource {
        return DataPointLocalDataSource(dataPointDao)
    }


}