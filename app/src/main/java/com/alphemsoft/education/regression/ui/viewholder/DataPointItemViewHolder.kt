package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import com.alphemsoft.education.regression.data.model.DataPoint
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class DataPointItemViewHolder(viewBinding: ItemDataPointBinding) : BaseItemViewHolder<ItemDataPointBinding, DataPoint>(
    viewBinding
) {
    override fun bind(item: DataPoint?) {
        mViewBinding.dataPoint = item
        mViewBinding.tvPositionNumber.text = absoluteAdapterPosition.plus(1).toString()
        mViewBinding.cbSelected.visibility =  if (isSelectable){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    override var isSelectable: Boolean = false
}