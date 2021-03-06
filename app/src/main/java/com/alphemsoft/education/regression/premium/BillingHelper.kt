package com.alphemsoft.education.regression.premium

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

class BillingHelper{
    private var skuDetails: List<SkuDetails>? = null
    private lateinit var billingClient: BillingClient

    fun initialize(
        context: Context,
        billingClientStateListener: BillingClientStateListener,
        purchasesUpdatedListener: PurchasesUpdatedListener
        ) {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(billingClientStateListener)
    }

    fun restartConnection(billingClientStateListener: BillingClientStateListener) {
        billingClient.startConnection(billingClientStateListener)
    }

    fun queryPurchases(): Purchase.PurchasesResult{
        return billingClient.queryPurchases(BillingClient.SkuType.SUBS)
    }

    private suspend fun getSkuDetailsFromGoogle(): List<SkuDetails>? {
        require(this::billingClient.isInitialized) { "Billing client not initialized" }
        val availableItems =
            listOf(
                SubscriptionPlan.MONTHLY_SUBSCRIPTION.subscriptionType,
                SubscriptionPlan.YEARLY_SUBSCRIPTION.subscriptionType
            )
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(availableItems)
            .setType(BillingClient.SkuType.SUBS)
            .build()
        val skuDetails = billingClient.querySkuDetails(skuDetailsParams)
        skuDetails
        return skuDetails.skuDetailsList
    }

    suspend fun querySkuDetails(): List<SkuDetails>? {
        val completableDeferred = getSkuDetailsFromGoogle()
        return completableDeferred
    }

    suspend fun querySubscriptionHistory(): PurchaseHistoryResult {
        return billingClient.queryPurchaseHistory(BillingClient.SkuType.SUBS)
    }

    fun launchBillingFlow(activity: AppCompatActivity, billingFlowParams: BillingFlowParams) {
        require(this::billingClient.isInitialized) { "Billing client not initialized" }
        val code = billingClient.launchBillingFlow(activity, billingFlowParams)?.responseCode

    }

    fun deviceSupportPurchases(): Boolean{
        val supported = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
            .responseCode == BillingClient.BillingResponseCode.OK
        return supported
    }

    enum class SubscriptionPlan(val subscriptionType: String) {
        MONTHLY_SUBSCRIPTION("premium_monthly_subscription"),
        YEARLY_SUBSCRIPTION("premium_yearly_subscription"),
        NO_PLAN("no_plan")
    }
}
