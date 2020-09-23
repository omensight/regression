package com.alphemsoft.education.regression.di

import android.content.Context
import androidx.room.Room
import com.alphemsoft.education.regression.data.RegressionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesRegressionDatabase(@ApplicationContext context: Context): RegressionDatabase {
        return Room.databaseBuilder(context, RegressionDatabase::class.java, "regression.db")
            .build()
    }

    @Provides
    fun providesDataPointDao(db: RegressionDatabase) = db.dataPointDao()

    @Provides
    fun providesSheetDao(db: RegressionDatabase) = db.sheetDao()

    @Provides
    fun providesSheetColumnDao(db:RegressionDatabase) = db.sheetColumnDao()
}