package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.databinding.ItemDataEntryBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityPagingDataAdapter
import com.alphemsoft.education.regression.ui.comparators.DbEntityComparatorItemCallback
import com.alphemsoft.education.regression.ui.viewholder.DataEntryItemViewHolder
import javax.inject.Inject

class DataPointPagingAdapter @Inject constructor() :
    BaseEntityPagingDataAdapter<DataEntry, DataEntryItemViewHolder>(
        DbEntityComparatorItemCallback()
    ) {
    override fun onBindViewHolder(holder: DataEntryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DataEntryItemViewHolder {
        val bind = ItemDataEntryBinding.inflate(inflater, parent, false)
        parent.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
        val width = parent.measuredWidth
        return DataEntryItemViewHolder(bind)
    }
}