package com.alphemsoft.education.regression.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding

import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.viewholder.DataPointItemViewHolder
import javax.inject.Inject

class DataPointAdapter @Inject constructor() :
    BaseEntityAdapter<SheetEntry, DataPointItemViewHolder>() {
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
    ): DataPointItemViewHolder {
        val binding = ItemDataPointBinding.inflate(inflater,parent,false)
        return DataPointItemViewHolder(binding, metrics)

    }

    override fun onViewRecycled(holder: DataPointItemViewHolder) {
        super.onViewRecycled(holder)
        holder.lostFocus()
    }

//    override fun getItemViewType(position: Int): Int {
//        return if (items[position] is DataPoint){
//            SimpleContentViewType.CONTENT.viewType
//        }else{
//            SimpleContentViewType.AD.viewType
//        }
//    }

    override fun onBindViewHolder(holder: DataPointItemViewHolder, position: Int) {
        holder.isSelectable = selectable
        holder.bind(items[position])
    }


}