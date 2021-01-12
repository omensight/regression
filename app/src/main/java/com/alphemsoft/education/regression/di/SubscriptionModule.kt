package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.RegressionDatabase
import com.alphemsoft.education.regression.data.dao.SubscriptionDao
import com.alphemsoft.education.regression.data.datasource.ISubscriptionDataSource
import com.alphemsoft.education.regression.data.datasource.base.SubscriptionDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SubscriptionModule {

    @Singleton
    @Provides
    fun providesSubscriptionDataSource(subscriptionDao: SubscriptionDao): ISubscriptionDataSource
            = SubscriptionDataSource(subscriptionDao)


}