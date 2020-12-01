package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.ui.OnAdLoadedListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Job
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseAppCompatActivity(private val supportsNativeAds: Boolean = false): AppCompatActivity(),
    OnAdLoadedListener {
    protected val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)

    private lateinit var adLoader: AdLoader
    private val adLoadedListeners: MutableList<OnAdLoadedListener> = ArrayList()
    private var unifiedNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTestDevices()
        if (supportsNativeAds){
            addAdLoadListener(this)
        }
        setupNativeAds()
    }

    private fun setupNativeAds() {
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
//        val testDeviceIds = java.util.ArrayList<String>()
//        testDeviceIds.addAll(resources.getStringArray(R.array.test_device_ids))

        val requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(
            listOf("10DBF36A467D9FB6AC6E5C94616189CC")
        ).build()


//        val requestConfiguration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(requestConfiguration)
    }

}