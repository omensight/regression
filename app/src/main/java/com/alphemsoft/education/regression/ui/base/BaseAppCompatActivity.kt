package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.alphemsoft.education.regression.ui.activity.NativeAdDispatcher
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Job
import com.alphemsoft.education.regression.R

abstract class BaseAppCompatActivity<VDB: ViewDataBinding>(
    private val layoutId: Int
): AppCompatActivity(){
    protected val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected lateinit var mDataBinding: VDB
    lateinit var nativeAdDispatcher: NativeAdDispatcher
        private set
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTestDevices()
        nativeAdDispatcher = NativeAdDispatcher(this)
        setupDataBinding()
    }

    private fun setupDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
    }

    private fun setupTestDevices() {
        val requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(
            resources.getStringArray(R.array.test_devices).toList()
        ).build()
        MobileAds.setRequestConfiguration(requestConfiguration)
    }

}