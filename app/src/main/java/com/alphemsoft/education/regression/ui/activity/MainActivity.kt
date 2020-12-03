package com.alphemsoft.education.regression.ui.activity

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.databinding.MainActivityBinding
import com.alphemsoft.education.regression.ui.base.BaseAppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class AbstractMainActivity: BaseAppCompatActivity<MainActivityBinding>(true, R.layout.main_activity)

@AndroidEntryPoint
class MainActivity : AbstractMainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(applicationContext)
        setupUi()
        loadAds()
        setupToolbar()
    }

    private fun setupUi() {
        mDataBinding.layoutPremiumSubscription.root.background =
            ContextCompat.getDrawable(applicationContext, R.drawable.drawable_shape_ripple_no_border)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            toolbar.elevation
        }
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.main_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    private fun showAds(unifiedNativeAds: List<AdEntity>) {
        if (unifiedNativeAds.isEmpty()){
            mDataBinding.layoutPremiumSubscription.root.visibility = View.VISIBLE
            mDataBinding.adTemplateView.visibility = View.GONE
        }else{
            mDataBinding.layoutPremiumSubscription.root.visibility = View.GONE
            mDataBinding.adTemplateView.visibility = View.VISIBLE
        }
        coroutineHandler.foregroundScope.launch {
            if (unifiedNativeAds.isNotEmpty()){
                unifiedNativeAds.forEachIndexed {i, ad->
                    ad.unifiedNativeAd?.let { nativeAd->
                        ad_template_view.setNativeAd(nativeAd)

                    }
                    Log.d("$i -> Coroutine Thread", "The thread is ${Thread.currentThread().name}, the ad is: ${ad.id}")
                    delay(1000*resources.getInteger(R.integer.ads_duration).toLong())
                    unifiedNativeAds.lastOrNull()?.let {
                        if(i == unifiedNativeAds.indexOf(it)){
                            loadAds()
                        }
                    }
                }
            }else{
                delay(1000*resources.getInteger(R.integer.ads_duration).toLong())
                loadAds()
            }
        }
    }

    override fun onAdsLoaded(unifiedNativeAds: List<AdEntity>, adsChanged: Boolean) {
        super.onAdsLoaded(unifiedNativeAds, adsChanged)
        println("AdsLoaded")
        showAds(unifiedNativeAds)
    }
}