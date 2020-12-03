package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class AdViewHolder(mViewBinding: ItemNativeAdBinding) :
    BaseItemViewHolder<ItemNativeAdBinding, AdEntity>(mViewBinding) {

    override var isSelectable: Boolean = false

    override fun bind(item: AdEntity?) {
        item?.unifiedNativeAd?.let {
            mDataBinding.adTemplateView.setNativeAd(it)
            mDataBinding.adTemplateView.visibility = View.VISIBLE
            mDataBinding.layoutPremiumSubscription.root.visibility = View.GONE
        } ?: run {
            mDataBinding.adTemplateView.visibility = View.GONE
            mDataBinding.layoutPremiumSubscription.root.visibility = View.VISIBLE
        }
    }
}