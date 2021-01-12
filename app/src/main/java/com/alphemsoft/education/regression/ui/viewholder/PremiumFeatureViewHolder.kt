package com.alphemsoft.education.regression.ui.viewholder

import androidx.core.content.ContextCompat
import com.alphemsoft.education.regression.data.model.secondary.PremiumFeature
import com.alphemsoft.education.regression.databinding.ItemPremiumFeatureBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class PremiumFeatureViewHolder(
    mDataBinding: ItemPremiumFeatureBinding,
) : BaseItemViewHolder<ItemPremiumFeatureBinding, PremiumFeature>(
    mDataBinding
) {
    override var isSelectable: Boolean = false
    override fun bind(item: PremiumFeature?) {
        item?.let {
            mDataBinding.icon.setImageDrawable(ContextCompat.getDrawable(context, item.icon))
            mDataBinding.tvDescription.text = context.getText(item.featureDescription)
            mDataBinding.premiumFeature = item
        }
    }
}