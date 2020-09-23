package com.alphemsoft.education.regression.extensions

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display

fun Activity.displayMetrics(): DisplayMetrics {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        val display: Display? = this.getDisplay()
        display!!.getRealMetrics(displayMetrics)
        displayMetrics
    }else{
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics
    }
}