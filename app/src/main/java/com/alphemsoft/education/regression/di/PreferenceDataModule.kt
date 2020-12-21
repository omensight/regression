package com.alphemsoft.education.regression.di

import com.alphemsoft.education.regression.data.datasource.IPreferenceDataSource
import com.alphemsoft.education.regression.data.datasource.base.PreferenceDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.xml.transform.OutputKeys

@Module
@InstallIn(ActivityComponent::class)
abstract class PreferenceDataModule{

    @Binds
    abstract fun bindsPreference(preferenceDataSource: PreferenceDataSource): IPreferenceDataSource
}