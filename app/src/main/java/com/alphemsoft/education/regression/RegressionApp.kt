package com.alphemsoft.education.regression

import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.HiltAndroidApp
import java.util.ArrayList
import javax.inject.Inject

@HiltAndroidApp
class RegressionApp : MultiDexApplication() {

}