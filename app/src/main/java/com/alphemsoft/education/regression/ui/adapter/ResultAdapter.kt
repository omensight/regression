package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.databinding.ItemNativeAdBinding
import com.alphemsoft.education.regression.databinding.ItemResultBinding
import com.alphemsoft.education.regression.enums.SimpleContentViewType
import com.alphemsoft.education.regression.ui.base.BaseEntityAdapter
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.viewholder.AdViewHolder
import com.alphemsoft.education.regression.ui.viewholder.ResultItemViewHolder

class ResultAdapter: BaseEntityAdapter<Any, BaseItemViewHolder<ViewDataBinding, Any>>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): BaseItemViewHolder<ViewDataBinding, Any> {
        return when(SimpleContentViewType.values()[viewType]){
            SimpleContentViewType.CONTENT -> {
                val binding = ItemResultBinding.inflate(inflater,parent,false)
                ResultItemViewHolder(binding)
            }
            SimpleContentViewType.AD -> {
                val binding = ItemNativeAdBinding.inflate(inflater,parent,false)
                AdViewHolder(binding)
            }
        } as BaseItemViewHolder<ViewDataBinding, Any>
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Result){
            SimpleContentViewType.CONTENT.viewType
        }else{
            SimpleContentViewType.AD.viewType
        }
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<ViewDataBinding, Any>, position: Int) {
        holder.bind(items[position])
    }
}