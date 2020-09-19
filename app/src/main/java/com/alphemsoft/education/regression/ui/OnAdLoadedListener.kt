package com.alphemsoft.education.regression.ui

import com.alphemsoft.education.regression.data.model.AdEntity

interface OnAdLoadedListener {
    fun onAdsLoaded(unifiedNativeAds: List<AdEntity>, adsChanged: Boolean)
}