package com.alphemsoft.education.regression.data.roomconverter

import androidx.room.TypeConverter
import com.alphemsoft.education.regression.premium.BillingHelper

class SubscriptionTypeRoomConverter {
    @TypeConverter
    fun subscriptionTypeToString(subscriptionPlan: BillingHelper.SubscriptionPlan?): String?{
        return subscriptionPlan?.subscriptionType
    }

    @TypeConverter
    fun stringToSubscriptionType(string: String?): BillingHelper.SubscriptionPlan?{
        return string?.let {
            BillingHelper.SubscriptionPlan.values().firstOrNull { it.subscriptionType == string }
        }
    }
}