package com.alphemsoft.education.regression

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.HiltAndroidApp
import java.util.ArrayList

@HiltAndroidApp
class RegressionApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTestDevices()
    }

    private fun setupTestDevices() {
        val testDeviceIds = ArrayList<String>()
        testDeviceIds.addAll(resources.getStringArray(R.array.test_device_ids))
        val requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(requestConfiguration)
    }
}