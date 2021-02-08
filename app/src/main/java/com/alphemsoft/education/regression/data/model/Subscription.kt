package com.alphemsoft.education.regression.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alphemsoft.education.regression.premium.BillingHelper
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState
import java.util.*

@Entity(tableName = "subscription")
data class Subscription(
    @ColumnInfo(name = "subscription_subscribed_at")
    val subscriptionTime: Date,
    @ColumnInfo(name = "subscription_subscription_state")
    val subscriptionState: PremiumSubscriptionState,
    @ColumnInfo(name = "subscription_time")
    val subscriptionPlan: BillingHelper.SubscriptionPlan
) {
    @PrimaryKey
    @ColumnInfo(name = "subscription_id")
    var id: Long = UNIQUE_ID

    companion object {
        const val UNIQUE_ID = 117L
    }

    fun getEndOfSubscriptionDate(): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = subscriptionTime
        return when (subscriptionPlan) {
            BillingHelper.SubscriptionPlan.MONTHLY_SUBSCRIPTION -> {
                calendar.add(Calendar.MONTH, 1)
                calendar.time
            }
            BillingHelper.SubscriptionPlan.YEARLY_SUBSCRIPTION -> {
                calendar.add(Calendar.YEAR, 1)
                calendar.time
            }
            BillingHelper.SubscriptionPlan.NO_PLAN -> null
        }
    }

    fun hasAnActiveSubscription(): Boolean {
        return subscriptionPlan != BillingHelper.SubscriptionPlan.NO_PLAN
    }
}