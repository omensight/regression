package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.secondary.PremiumFeature
import com.alphemsoft.education.regression.databinding.FragmentPurchaseSubscriptionBinding
import com.alphemsoft.education.regression.premium.BillingHelper
import com.alphemsoft.education.regression.ui.adapter.PremiumFeatureAdapter
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.PurchaseSubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class AbstractBuySubscriptionFragment :
    BaseFragment<FragmentPurchaseSubscriptionBinding, PurchaseSubscriptionViewModel>(
        R.layout.fragment_purchase_subscription,
        BR.buySubscriptionViewModel
    )

@AndroidEntryPoint
class PurchaseSubscriptionFragment : AbstractBuySubscriptionFragment() {
    override val viewModel: PurchaseSubscriptionViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initialize()
    }

    override fun onResume() {
        super.onResume()
        initializeUi()
        initializeSkuList()
    }

    private fun initializeSkuList() {
        coroutineHandler.foregroundScope.launch {
            viewModel.skuDetailsFlow.collectLatest {
                val monthlySkuDetails =
                    it?.firstOrNull { skuDetails ->
                        skuDetails.sku == BillingHelper.SubscriptionPlan.MONTHLY_SUBSCRIPTION.subscriptionType }
                val yearlySubsSkuDetails =
                    it?.firstOrNull {skuDetails ->
                        skuDetails.sku == BillingHelper.SubscriptionPlan.YEARLY_SUBSCRIPTION.subscriptionType }
                monthlySkuDetails?.let {
                    dataBinding.apply {
                        tvPriceMonthly.text = monthlySkuDetails.price
                    }
                }
                yearlySubsSkuDetails?.let {
                    dataBinding.apply {
                        tvPriceYearly.text = yearlySubsSkuDetails.price
                    }
                }
            }
        }
        coroutineHandler.foregroundScope.launch {
            viewModel.supportSubscriptionLiveData.asFlow().collectLatest {
                it?.let { supportsSubscription ->
                    if (supportsSubscription){
                        dataBinding.tvApologize.visibility = View.GONE
                        dataBinding.tvPayAsYouGoMessage.visibility = View.VISIBLE
                        dataBinding.rvFeatures.visibility = View.VISIBLE
                    }else{
                        dataBinding.tvApologize.visibility = View.VISIBLE
                        dataBinding.tvPayAsYouGoMessage.visibility = View.GONE
                        dataBinding.rvFeatures.visibility = View.GONE
                    }
                }
            }
        }
    }

    private val premiumFeatureAdapter = PremiumFeatureAdapter()

    private fun initializeUi() {
        premiumFeatureAdapter.addNewItems(
            listOf(
                PremiumFeature(
                    R.drawable.ic_ads_blocked,
                    R.string.premium_feature_description_no_ads
                )
            )
        )
        dataBinding.rvFeatures.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = premiumFeatureAdapter
        }
        dataBinding.viewBackgroundMonthlySubs.setOnClickListener {
            coroutineHandler.backgroundScope.launch {
                viewModel.startSubscriptionPurchaseFlow(
                    mActivity,
                    BillingHelper.SubscriptionPlan.MONTHLY_SUBSCRIPTION.subscriptionType
                )
            }
        }

        dataBinding.viewBackgroundYearlySubs.setOnClickListener {
            coroutineHandler.backgroundScope.launch {
                viewModel.startSubscriptionPurchaseFlow(
                    mActivity,
                    BillingHelper.SubscriptionPlan.YEARLY_SUBSCRIPTION.subscriptionType
                )
            }
        }

    }
}