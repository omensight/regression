package com.alphemsoft.education.regression.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alphemsoft.education.regression.data.model.Subscription
import com.alphemsoft.education.regression.premium.BillingHelper
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SubscriptionDao: BaseDao<Subscription>{

    @Query("SELECT * FROM subscription WHERE subscription_id = 117")
    fun getUniqueSubscriptionFlow(): Flow<Subscription>

    @Query("SELECT * FROM subscription WHERE subscription_id = 117")
    suspend fun getUniqueSubscription(): Subscription?

    @Transaction
    suspend fun setSubscriptionAsExpired() {
        update(Subscription(Date(-1), PremiumSubscriptionState.NOT_SUBSCRIBED, BillingHelper.SubscriptionPlan.NO_PLAN))
    }

    @Transaction
    suspend fun grantTemporaryAccess(): Boolean {
        val temporarySubscription = Subscription(Date(),PremiumSubscriptionState.TEMPORARY_ACCESS, BillingHelper.SubscriptionPlan.NO_PLAN)
        update(temporarySubscription)
        return true
    }

}