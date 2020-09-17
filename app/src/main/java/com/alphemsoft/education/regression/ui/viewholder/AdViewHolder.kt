package com.alphemsoft.education.regression.ui.viewholder

import androidx.lifecycle.liveData
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.launch

class AdViewHolder(mViewBinding: ItemNativeAdBinding) :
    BaseItemViewHolder<ItemNativeAdBinding, AdEntity>(mViewBinding) {


    override fun bind(item: AdEntity?) {
        item?.unifiedNativeAd?.let {
            mViewBinding.adTemplateView.setNativeAd(it)
        } ?: run {

        }
    }

    override var isSelectable: Boolean = false
}