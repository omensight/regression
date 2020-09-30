package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.dao.SheetDao
import com.alphemsoft.education.regression.data.datasource.ISheetDataSource
import com.alphemsoft.education.regression.data.datasource.SheetLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object SheetDataModule {
    @Provides fun provideSheetDataSource(sheetDao: SheetDao): ISheetDataSource = SheetLocalDataSource(sheetDao)
}