package com.alphemsoft.education.regression.data.roomconverter

import androidx.room.TypeConverter
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState

class PremiumSubscriptionTypeConverter {
    @TypeConverter
    fun premiumSubscriptionStateToInt(subscriptionType: PremiumSubscriptionState) = subscriptionType.state

    @TypeConverter
    fun intToPremiumSubscriptionState(subscriptionStateInt: Int) = PremiumSubscriptionState.values()[subscriptionStateInt]
}