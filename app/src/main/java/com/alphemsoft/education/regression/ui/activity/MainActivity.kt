package com.alphemsoft.education.regression.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.MainActivityBinding
import com.alphemsoft.education.regression.premium.PremiumSubscriptionState
import com.alphemsoft.education.regression.ui.base.BaseAppCompatActivity
import com.alphemsoft.education.regression.viewmodel.PurchaseSubscriptionViewModel
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

abstract class AbstractMainActivity :
    BaseAppCompatActivity<MainActivityBinding>(R.layout.main_activity)

@AndroidEntryPoint
class MainActivity : AbstractMainActivity() {

    val subscriptionViewModel: PurchaseSubscriptionViewModel by viewModels()
    private val adJob = Job()
    private val backgroundAdCoroutineScope = CoroutineScope(adJob + Dispatchers.Default)
    private val foregroundAdCoroutineScope = CoroutineScope(adJob + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(applicationContext)
        setupUi()
        setupAds()
    }

    private fun setupAds() {
        coroutineHandler.foregroundScope.launch {
            subscriptionViewModel.subscriptionFlow.collectLatest {subscription ->
                when(subscription.subscriptionState){
                    PremiumSubscriptionState.NOT_SUBSCRIBED, PremiumSubscriptionState.TEMPORARY_ACCESS -> {
                        mDataBinding.layoutPremiumSubscription.root.visibility = View.VISIBLE
                        showAds()
                    }
                    PremiumSubscriptionState.SUBSCRIBED, PremiumSubscriptionState.CANCELLED -> {
                        adJob.cancel()
                        mDataBinding.layoutPremiumSubscription.root.visibility = View.GONE
                        mDataBinding.adTemplateView.visibility = View.GONE
                        mDataBinding.layoutPremiumSubscription.root.invalidate()
                    }
                }
            }
        }
    }

    private fun setupUi() {
        mDataBinding.layoutPremiumSubscription.root.background =
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.drawable_shape_ripple_no_border
            )
        mDataBinding.layoutPremiumSubscription.btSubscribe.setOnClickListener {
            findNavController(R.id.main_nav_host_fragment).navigate(R.id.destination_purchase_subscription)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation
        }
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.main_nav_host_fragment))
                || super.onOptionsItemSelected(item) || when(item.itemId){
            R.id.main_menu_settings -> {
                findNavController(R.id.main_nav_host_fragment).navigate(R.id.destination_main_settings)
                true
            }
            else -> false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun showAds() {
        backgroundAdCoroutineScope.launch {
            val nativeAds = nativeAdDispatcher.fetchAds()

            foregroundAdCoroutineScope.launch {
                if (nativeAds.isEmpty()) {
                    mDataBinding.layoutPremiumSubscription.root.visibility = View.VISIBLE
                    mDataBinding.adTemplateView.visibility = View.GONE
                } else {
                    mDataBinding.layoutPremiumSubscription.root.visibility = View.GONE
                    mDataBinding.adTemplateView.visibility = View.VISIBLE
                }
                nativeAds.forEach { nativeAd ->
                    nativeAd.unifiedNativeAd?.let {
                        ad_template_view.setNativeAd(it)
                    }
                    delay(1000 * resources.getInteger(R.integer.ad_duration).toLong())
                }
                if (nativeAds.isEmpty()){
                    delay(1000*5.toLong())
                }else{
                    delay(1000*30.toLong())
                }
                showAds()
            }

        }
    }
}