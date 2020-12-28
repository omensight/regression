package com.alphemsoft.education.regression.ui.activity

import android.content.Context
import android.util.Log
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.CompletableDeferred

class NativeAdDispatcher(private val context: Context) {

    private val nativeAds: MutableList<NativeAdEntity> = ArrayList()
    private var isLoading = false

    suspend fun fetchAds(): List<NativeAdEntity> {
        val thereIsUnusedAds = nativeAds.any {
            it.isUsed == false
        }
        val newNativeAds: List<NativeAdEntity> = ArrayList(nativeAds)
        if (!thereIsUnusedAds && !isLoading) {
            isLoading = true
            nativeAds.addAll(getNativeAds())
            isLoading = false
        }
        return newNativeAds
    }

    private suspend fun getNativeAds(): List<NativeAdEntity>{
        val ref = CompletableDeferred<List<NativeAdEntity>>()
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
            Log.println(Log.DEBUG,"Executed","Loaded")

            if (adLoader?.isLoading != true) {
                this.nativeAds.clear()
                this.nativeAds.addAll(newAds)
            }else{
                ref.complete(ArrayList(nativeAds))
            }
        }

        adLoaderBuilder.withAdListener(object : AdListener() {

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                ref.complete(emptyList())
            }
        })
        adLoader = adLoaderBuilder.build()
        adLoader.loadAds(AdRequest.Builder().build(),5)
        return ref.await()
    }
}