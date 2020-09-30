package com.alphemsoft.education.regression.ui.viewholder

import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class AdViewHolder(mViewBinding: ItemNativeAdBinding) :
    BaseItemViewHolder<ItemNativeAdBinding, AdEntity>(mViewBinding) {


    override fun bind(item: AdEntity?) {
        item?.unifiedNativeAd?.let {
            mDataBinding.adTemplateView.setNativeAd(it)
        } ?: run {

        }
    }

    override var isSelectable: Boolean = false
}