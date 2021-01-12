package com.alphemsoft.education.regression.premium.feature

interface PremiumFeature {
    suspend fun execute(): Boolean
}