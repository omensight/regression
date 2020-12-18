package com.alphemsoft.education.regression.ui.activity

import android.content.Context
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NativeAdDispatcher(private val context: Context) {

    private val nativeAds: MutableList<NativeAdEntity> = ArrayList()

    suspend fun fetchAds(): List<NativeAdEntity> {
        val thereIsUnusedAds = nativeAds.map { it.isUsed }.any { it }
        var newNativeAds: List<NativeAdEntity> = ArrayList(nativeAds)
        if (!thereIsUnusedAds) {
            newNativeAds = getNativeAds()
        }
        return newNativeAds
    }

    private suspend fun getNativeAds(): List<NativeAdEntity> = suspendCoroutine { continuation ->
        var adLoader: AdLoader? = null
        val adLoaderBuilder =
            AdLoader.Builder(context, context.getString(R.string.ad_native_id))
        val newAds = mutableListOf<NativeAdEntity>()
        adLoaderBuilder.forUnifiedNativeAd { nativeAd ->
            nativeAd?.let { unifiedAdEntity ->
                newAds.add(NativeAdEntity().apply {
                    this.unifiedNativeAd = unifiedAdEntity
                })
            }
            if (adLoader?.isLoading != true) {
                this.nativeAds.clear()
                this.nativeAds.addAll(newAds)
                continuation.resume(ArrayList(nativeAds))
            }
        }

        adLoaderBuilder.withAdListener(object : AdListener() {

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                println()
                continuation.resume(listOf(NativeAdEntity()))
            }
        })
        adLoader = adLoaderBuilder.build()
        adLoader.loadAds(AdRequest.Builder().build(),5)
    }

}