package com.alphemsoft.education.regression.ui.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.ui.OnAdLoadedListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Job

abstract class BaseAppCompatActivity<VDB: ViewDataBinding>(
    private val supportsNativeAds: Boolean = false,
    private val layoutId: Int
): AppCompatActivity(),
    OnAdLoadedListener {
    protected val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected lateinit var mDataBinding: VDB
    private lateinit var adLoader: AdLoader
    private val adLoadedListeners: MutableList<OnAdLoadedListener> = ArrayList()
    private var unifiedNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupTestDevices()

        setupNativeAds()
    }

    private fun setupDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
    }

    private fun setupNativeAds() {
        if (supportsNativeAds){
            addAdLoadListener(this)
        }
        val adLoaderBuilder =
            AdLoader.Builder(applicationContext, getString(R.string.ad_native_id))
        adLoaderBuilder.forUnifiedNativeAd { nativeAd ->
            nativeAd?.let {
                unifiedNativeAds.add(it)
            }
            if (!adLoader.isLoading){
                val ads = unifiedNativeAds.map {
                    AdEntity().apply {
                        unifiedNativeAd = it
                    }
                }
                adLoadedListeners.forEach {
                    it.onAdsLoaded(ads, true)
                }
            }
        }



        adLoaderBuilder.withAdListener(object: AdListener() {

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                adLoadedListeners.forEach {
                    val listOfAds = emptyList<AdEntity>()
                    it.onAdsLoaded(listOfAds, true)
                }
            }
        })
        adLoader = adLoaderBuilder.build()
    }

    protected fun loadAds(){
        unifiedNativeAds.clear()
        adLoader.loadAds(AdRequest.Builder().build(), 5)
    }

    fun getUnifiedNativeAds(): List<UnifiedNativeAd>{
        return unifiedNativeAds
    }

    fun addAdLoadListener(adLoadedListener: OnAdLoadedListener){
        if (!adLoadedListeners.contains(adLoadedListener)){
            adLoadedListeners.add(adLoadedListener)
            val ads = if (unifiedNativeAds.isEmpty()){
                emptyList()
            }else{
                unifiedNativeAds.map {
                    AdEntity().apply {
                        unifiedNativeAd = it
                    }
                }
            }
            adLoadedListener.onAdsLoaded(ads,false)
        }
    }

    @CallSuper
    override fun onAdsLoaded(unifiedNativeAds: List<AdEntity>, adsChanged: Boolean) {
        require(supportsNativeAds){"Load native ads not supported"}
    }

    private fun setupTestDevices() {
        val requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(
            listOf("10DBF36A467D9FB6AC6E5C94616189CC")
        ).build()


        MobileAds.setRequestConfiguration(requestConfiguration)
    }

}