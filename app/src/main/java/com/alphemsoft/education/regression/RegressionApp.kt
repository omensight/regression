package com.alphemsoft.education.regression

import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.HiltAndroidApp
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.util.ArrayList
import javax.inject.Inject

@HiltAndroidApp
class RegressionApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }
}