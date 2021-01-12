package com.alphemsoft.education.regression.data.datasource.base

import androidx.room.Transaction
import com.alphemsoft.education.regression.data.dao.SubscriptionDao
import com.alphemsoft.education.regression.data.datasource.ISubscriptionDataSource
import com.alphemsoft.education.regression.data.model.Subscription
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class SubscriptionDataSource(private val subscriptionDao: SubscriptionDao) :
    ISubscriptionDataSource {
    override fun getUniqueSubscriptionFlow(): Flow<Subscription> {
        return subscriptionDao.getUniqueSubscriptionFlow()
    }

    override suspend fun getUniqueSubscription(): Subscription? {
        return subscriptionDao.getUniqueSubscription()
    }

    override suspend fun find(id: Long): Subscription? {
        return subscriptionDao.getUniqueSubscription()
    }

    override suspend fun insert(item: Subscription): Long {
        return subscriptionDao.insert(item)
    }

    override suspend fun insert(items: List<Subscription>) {

    }

    override suspend fun delete(items: List<Subscription>) {

    }

    override suspend fun update(items: List<Subscription>) {
        subscriptionDao.update(items)
    }

    override suspend fun setSubscriptionAsExpired() {
        subscriptionDao.setSubscriptionAsExpired()
    }

    override suspend fun grantTemporaryAccess(): Boolean {
        return subscriptionDao.grantTemporaryAccess()
    }

}