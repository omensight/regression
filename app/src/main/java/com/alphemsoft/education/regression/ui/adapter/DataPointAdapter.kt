package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.enums.SimpleContentViewType

import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.viewholder.AdViewHolder
import com.alphemsoft.education.regression.ui.viewholder.DataPointItemViewHolder
import javax.inject.Inject

class DataPointAdapter @Inject constructor() :
    BaseEntityAdapter<DataPoint, DataPointItemViewHolder>() {
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
        return DataPointItemViewHolder(binding)

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