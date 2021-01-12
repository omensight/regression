package com.alphemsoft.education.regression.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.datasource.ISubscriptionDataSource
import com.alphemsoft.education.regression.data.model.Subscription
import com.alphemsoft.education.regression.premium.BillingHelper
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState
import com.android.billingclient.api.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class PurchaseSubscriptionViewModel @ViewModelInject constructor(
    application: Application,
    private val subscriptionDataSource: ISubscriptionDataSource,
) : AndroidViewModel(application), BillingClientStateListener, PurchasesUpdatedListener {
    private lateinit var skuDetails: List<SkuDetails>

    val subscriptionFlow = subscriptionDataSource.getUniqueSubscriptionFlow()

    private val skuDetailsLiveData: MutableLiveData<List<SkuDetails>> = MutableLiveData()
    val skuDetailsFlow = skuDetailsLiveData.asFlow()
    private val coroutineHandler = CoroutineHandler(Job())
    private val billingHelper = BillingHelper()

    init {
        coroutineHandler.backgroundScope.launch {
            billingHelper.initialize(
                application,
                this@PurchaseSubscriptionViewModel,
                this@PurchaseSubscriptionViewModel
            )

        }
    }

    /**
     * This method is used for start the subscription flow
     * @param activity is the activity from where the flow is launched
     * @param skuId is the sku id
     * @return true if the flow have been launched and false if the current user already have been purchased a subscription
     */
    suspend fun startSubscriptionPurchaseFlow(activity: AppCompatActivity, skuId: String): Boolean {
        var launched: Boolean? = null
        val dataBaseSubscription = subscriptionDataSource.find(Subscription.UNIQUE_ID)
        dataBaseSubscription?.let {
            launched = when (dataBaseSubscription.subscriptionState) {
                PremiumSubscriptionState.NOT_SUBSCRIBED, PremiumSubscriptionState.TEMPORARY_ACCESS -> {
                    skuDetails.firstOrNull { it.sku == skuId }?.let { foundSkuDetails ->
                        val billingFLowParams =
                            BillingFlowParams.newBuilder().setSkuDetails(foundSkuDetails).build()
                        billingHelper.launchBillingFlow(activity, billingFLowParams)
                    }
                    true
                }
                PremiumSubscriptionState.CANCELLED, PremiumSubscriptionState.SUBSCRIBED -> false
            }
        }
        return launched ?: false
    }

    override fun onBillingSetupFinished(p0: BillingResult) {
        val purchases = billingHelper.queryPurchases()
        val currentPurchase = purchases.purchasesList?.firstOrNull()
        coroutineHandler.backgroundScope.launch {
            currentPurchase?.let {
                subscriptionDataSource.getUniqueSubscription()?.let { subscription ->
                    val currentSubscriptionPlan =
                        BillingHelper.SubscriptionPlan.values()
                            .firstOrNull{ it.subscriptionType == currentPurchase.sku}
                            ?:BillingHelper.SubscriptionPlan.NO_PLAN
                    val premiumSubscriptionState = when(currentPurchase.purchaseState){
                        BillingClient.BillingResponseCode.OK ->  PremiumSubscriptionState.SUBSCRIBED
                        BillingClient.BillingResponseCode.USER_CANCELED -> PremiumSubscriptionState.CANCELLED
                        else -> PremiumSubscriptionState.NOT_SUBSCRIBED
                    }
                    val currentPurchaseTime = Date(currentPurchase.purchaseTime)
                    if (subscription.subscriptionState != premiumSubscriptionState
                        && subscription.subscriptionPlan != currentSubscriptionPlan
                        && subscription.subscriptionTime != currentPurchaseTime){
                        val updatedSubscription = subscription.copy(
                            subscriptionTime = currentPurchaseTime,
                            subscriptionState = premiumSubscriptionState,
                            subscriptionPlan = currentSubscriptionPlan
                        )
                        subscriptionDataSource.update(listOf(updatedSubscription))
                    }

                }
            }
            Log.d("BillingResponseCode", purchases.purchasesList.toString())
            val subscriptionHistory = billingHelper.querySubscriptionHistory()
            skuDetails = billingHelper.querySkuDetails()
            if (skuDetails.isEmpty()) {
                coroutineHandler.backgroundScope.launch {
                    delay(1000)
                    billingHelper.restartConnection(this@PurchaseSubscriptionViewModel)
                }
            }
            skuDetailsLiveData.postValue(skuDetails)
        }
    }

    override fun onBillingServiceDisconnected() {
        coroutineHandler.backgroundScope.launch {
            delay(5000)
            billingHelper.restartConnection(this@PurchaseSubscriptionViewModel)
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchases?.let { safePurchases ->
                if (safePurchases.isNotEmpty()) {
                    val purchase = purchases[0]
                    val acquiredOn = purchase.purchaseTime
                    val subscriptionPlan = BillingHelper.SubscriptionPlan.values()
                        .first { it.subscriptionType == purchase.sku }
                    val subscription = Subscription(
                        Date(acquiredOn),
                        PremiumSubscriptionState.SUBSCRIBED,
                        subscriptionPlan
                    )
                    coroutineHandler.backgroundScope.launch {
                        subscriptionDataSource.update(listOf(subscription))
                    }
                }
            }
        } else {
            Toast.makeText(getApplication(), R.string.cancelled_purchase, Toast.LENGTH_SHORT).show()
        }
    }
}