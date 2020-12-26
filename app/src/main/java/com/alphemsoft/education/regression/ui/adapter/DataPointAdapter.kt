package com.alphemsoft.education.regression.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding

import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.comparators.DataEntryComparator
import com.alphemsoft.education.regression.ui.viewholder.DataPairViewHolder
import javax.inject.Inject

class DataPointAdapter @Inject constructor() :
    BaseEntityAdapter<SheetEntry, DataPairViewHolder>(DataEntryComparator()) {
    lateinit var metrics: DisplayMetrics
    var selectable: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DataPairViewHolder {
        val binding = ItemDataPointBinding.inflate(inflater,parent,false)
        return DataPairViewHolder(binding, metrics)

    }

    override fun onViewRecycled(holder: DataPairViewHolder) {
        super.onViewRecycled(holder)
        holder.lostFocus()
    }

    override fun onBindViewHolder(holder: DataPairViewHolder, position: Int) {
        holder.isSelectable = selectable
        val item = items[holder.adapterPosition]
        holder.bind(item)
    }


}