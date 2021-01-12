package com.alphemsoft.education.regression.data.datasource

import com.alphemsoft.education.regression.data.datasource.base.BaseDataSource
import com.alphemsoft.education.regression.data.model.Subscription
import kotlinx.coroutines.flow.Flow

interface ISubscriptionDataSource: BaseDataSource<Subscription, Long> {
    fun getUniqueSubscriptionFlow(): Flow<Subscription>
    suspend fun getUniqueSubscription(): Subscription?
    suspend fun setSubscriptionAsExpired()
    suspend fun grantTemporaryAccess(): Boolean
}