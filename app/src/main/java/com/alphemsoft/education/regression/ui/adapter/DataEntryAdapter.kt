package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.databinding.ItemDataEntryBinding

import com.alphemsoft.education.regression.ui.base.BaseAdapter
import com.alphemsoft.education.regression.ui.viewholder.DataEntryItemViewHolder
import javax.inject.Inject

class DataEntryAdapter @Inject constructor() :
    BaseAdapter<DataEntry, DataEntryItemViewHolder>() {
    var selectable: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DataEntryItemViewHolder {
        val binding = ItemDataEntryBinding.inflate(inflater,parent,false)
        val w = parent.width
        return DataEntryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataEntryItemViewHolder, position: Int) {
        holder.isSelectable = selectable
        holder.bind(items[position])
    }


}