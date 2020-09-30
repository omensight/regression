package com.alphemsoft.education.regression.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alphemsoft.education.regression.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideNavController(activity: Activity): NavController{
        return activity.findNavController(R.id.main_nav_host_fragment)
    }
}