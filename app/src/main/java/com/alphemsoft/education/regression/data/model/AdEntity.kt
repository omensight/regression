package com.alphemsoft.education.regression.data.model

import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.util.*

class AdEntity {
    var unifiedNativeAd: UnifiedNativeAd? = null
    val id = UUID.randomUUID().toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdEntity

        if (unifiedNativeAd != other.unifiedNativeAd) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unifiedNativeAd?.hashCode() ?: 0
        result = 31 * result + id.hashCode()
        return result
    }


}