package com.alphemsoft.education.regression.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.dao.DataPointDao
import com.alphemsoft.education.regression.data.dao.PreferenceDao
import com.alphemsoft.education.regression.data.dao.SheetDao
import com.alphemsoft.education.regression.data.dao.SubscriptionDao
import com.alphemsoft.education.regression.data.model.PreferenceModel
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.data.model.Subscription
import com.alphemsoft.education.regression.data.roomconverter.BigDecimalConverter
import com.alphemsoft.education.regression.data.roomconverter.DateRoomConverters
import com.alphemsoft.education.regression.data.roomconverter.PremiumSubscriptionTypeConverter
import com.alphemsoft.education.regression.data.roomconverter.SubscriptionTypeRoomConverter
import com.alphemsoft.education.regression.premium.BillingHelper
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@Database(
    entities = [Sheet::class, SheetEntry::class, PreferenceModel::class, Subscription::class],
    version = 1
)
@TypeConverters(
    DateRoomConverters::class,
    BigDecimalConverter::class,
    PremiumSubscriptionTypeConverter::class,
    SubscriptionTypeRoomConverter::class
)
abstract class RegressionDatabase : RoomDatabase() {
    val coroutineHandler = CoroutineHandler(Job())

    abstract fun sheetDao(): SheetDao
    abstract fun dataPointDao(): DataPointDao
    abstract fun preferenceDao(): PreferenceDao
    abstract fun subscriptionDao(): SubscriptionDao

    fun populateInitialData() {
        coroutineHandler.backgroundScope.launch {
            var uniqueSubscription = subscriptionDao().getUniqueSubscription()
            uniqueSubscription ?: run {
                runInTransaction {
                    uniqueSubscription = Subscription(Date(-1), PremiumSubscriptionState.NOT_SUBSCRIBED, BillingHelper.SubscriptionPlan.NO_PLAN)
                    subscriptionDao().insert(uniqueSubscription!!)
                }
            }
        }
    }

    fun checkSubscription(){
        coroutineHandler.backgroundScope.launch {
            coroutineHandler.backgroundScope.launch {
                val subscriptionOnDb = subscriptionDao().getUniqueSubscription()
                val now = Date()
                subscriptionOnDb?.getEndOfSubscriptionDate()?.let { endOfSubscription ->
                    if (now > endOfSubscription){
                        subscriptionDao().setSubscriptionAsExpired()
                    }
                }
            }
        }
    }
}