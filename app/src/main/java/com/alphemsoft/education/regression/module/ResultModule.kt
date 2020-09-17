package com.alphemsoft.education.regression.module

import com.alphemsoft.education.regression.ui.adapter.ResultAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object ResultModule {
    @Provides
    fun providesDataPointAdapter(): ResultAdapter {
        return ResultAdapter()
    }
}