package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.OnAdLoadedListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseAppCompatActivity(private val supportsNativeAds: Boolean = false): AppCompatActivity(),
    OnAdLoadedListener {
    protected val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)

    private lateinit var adLoader: AdLoader
    private val adLoadedListeners: MutableList<OnAdLoadedListener> = ArrayList()
    private var unifiedNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportsNativeAds){
            addAdLoadListener(this)
        }
        setupNativeAds()
        loadAds()
    }

    private fun setupNativeAds() {
        val adLoaderBuilder =
            AdLoader.Builder(applicationContext, getString(R.string.ad_native_id))
        adLoaderBuilder.forUnifiedNativeAd { nativeAd ->
            nativeAd?.let {
                unifiedNativeAds.add(it)
            }
            if (!adLoader.isLoading){
                adLoadedListeners.forEach {
                    it.onAdsLoaded(unifiedNativeAds, true)
                }
            }
        }
        adLoaderBuilder.withAdListener(object: AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }
        })
        adLoader = adLoaderBuilder.build()
    }

    protected fun loadAds(){
        adLoader.loadAds(AdRequest.Builder().build(), 5)
    }

    fun getUnifiedNativeAds(): List<UnifiedNativeAd>{
        return unifiedNativeAds
    }

    fun addAdLoadListener(adLoadedListener: OnAdLoadedListener){
        adLoadedListeners.add(adLoadedListener)
    }

    @CallSuper
    override fun onAdsLoaded(unifiedNativeAds: MutableList<UnifiedNativeAd>, adsChanged: Boolean) {
        require(supportsNativeAds){"Load native ads not supported"}
    }
}