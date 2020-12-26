package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityPagingDataAdapter
import com.alphemsoft.education.regression.ui.comparators.DbEntityComparatorItemCallback
import com.alphemsoft.education.regression.ui.viewholder.DataPairViewHolder
import javax.inject.Inject

class DataPointPagingAdapter @Inject constructor() :
    BaseEntityPagingDataAdapter<SheetEntry, DataPairViewHolder>(
        DbEntityComparatorItemCallback()
    ) {
    override fun onBindViewHolder(holder: DataPairViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): DataPairViewHolder {
        val bind = ItemDataPointBinding.inflate(inflater, parent, false)
        return DataPairViewHolder(bind)
    }
}