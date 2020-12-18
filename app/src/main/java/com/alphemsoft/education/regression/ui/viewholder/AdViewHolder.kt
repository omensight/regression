package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import com.alphemsoft.education.regression.data.model.NativeAdEntity
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class AdViewHolder(mViewBinding: ItemNativeAdBinding) :
    BaseItemViewHolder<ItemNativeAdBinding, NativeAdEntity>(mViewBinding) {

    override var isSelectable: Boolean = false

    override fun bind(item: NativeAdEntity?) {
        if (item?.unifiedNativeAd != null) {
            mDataBinding.adTemplateView.setNativeAd(item.unifiedNativeAd)
            mDataBinding.adTemplateView.visibility = View.VISIBLE
            mDataBinding.layoutPremiumSubscription.root.visibility = View.GONE
        }else{
            mDataBinding.adTemplateView.visibility = View.GONE
            mDataBinding.layoutPremiumSubscription.root.visibility = View.VISIBLE
        }
    }
}