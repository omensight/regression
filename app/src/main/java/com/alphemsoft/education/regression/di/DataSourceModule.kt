package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.datasource.ISheetColumnLocalDataSource
import com.alphemsoft.education.regression.data.datasource.SheetColumnLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindSheetColumnDataSource(sheetColumnLocalDataSource: SheetColumnLocalDataSource): ISheetColumnLocalDataSource
}