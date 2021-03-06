package com.alphemsoft.education.regression.di

import android.content.Context
import androidx.room.Room
import com.alphemsoft.education.regression.data.RegressionDatabase
import com.alphemsoft.education.regression.data.legacy.LegacyDataMigrationHelper
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
        val path = context.packageManager.getPackageInfo(context.packageName,0).applicationInfo.dataDir
        val dbPath = "$path/modern_regression.db"
        val database = Room.databaseBuilder(context, RegressionDatabase::class.java, dbPath)
            .build()
        database.populateInitialData()
        database.checkSubscription()
        return database
    }

    @Provides
    @Singleton
    fun providesDataPointDao(db: RegressionDatabase) = db.dataPointDao()

    @Provides
    @Singleton
    fun providesSheetDao(db: RegressionDatabase) = db.sheetDao()

    @Provides
    @Singleton
    fun providesPreferenceDao(db: RegressionDatabase) = db.preferenceDao()

    @Provides
    fun providesLegacyDataMigrationHelper(@ApplicationContext context: Context) = LegacyDataMigrationHelper(context)

    @Provides
    @Singleton
    fun providesSubscriptionDao(db: RegressionDatabase) = db.subscriptionDao()

}