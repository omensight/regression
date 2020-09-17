package com.alphemsoft.education.regression.ui

import com.google.android.gms.ads.formats.UnifiedNativeAd

interface OnAdLoadedListener {
    fun onAdsLoaded(unifiedNativeAds: MutableList<UnifiedNativeAd>, adsChanged: Boolean)
}