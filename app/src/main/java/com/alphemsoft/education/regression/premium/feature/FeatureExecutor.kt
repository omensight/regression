package com.alphemsoft.education.regression.premium.feature

import com.alphemsoft.education.regression.premium.BillingHelper

class FeatureExecutor(
    private val hasPremiumSubscription: suspend () -> Boolean,
    private val premiumFeature: PremiumFeature
) {
    suspend fun execute(): Boolean{
        if (hasPremiumSubscription.invoke()){
            premiumFeature.execute()
        }
        return true
    }

}