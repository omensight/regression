package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.secondary.Help
import com.alphemsoft.education.regression.databinding.ItemHelpBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.viewholder.HelpViewHolder

class HelpAdapter : BaseEntityAdapter<Help, HelpViewHolder>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): HelpViewHolder {
        val binding = ItemHelpBinding.inflate(inflater, parent, false)
        return HelpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(items[position])
    }
}