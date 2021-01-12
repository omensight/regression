package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.secondary.PremiumFeature
import com.alphemsoft.education.regression.databinding.ItemPremiumFeatureBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.viewholder.PremiumFeatureViewHolder

class PremiumFeatureAdapter: BaseEntityAdapter<PremiumFeature, PremiumFeatureViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): PremiumFeatureViewHolder {
        val dataBinding = ItemPremiumFeatureBinding.inflate(inflater, parent, false)
        return PremiumFeatureViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: PremiumFeatureViewHolder, position: Int) {
        holder.bind(items[position])
    }
}