package com.alphemsoft.education.regression.premium.feature

class ExportDataFeature(var registerName: String): PremiumFeature {
    override suspend fun execute(): Boolean {
        return true
    }

}