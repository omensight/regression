package com.alphemsoft.education.regression.ui.viewholder

import android.util.DisplayMetrics
import android.view.View
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class DataPointItemViewHolder(
    viewBinding: ItemDataPointBinding,
    private val metrics: DisplayMetrics? = null
) : BaseItemViewHolder<ItemDataPointBinding, SheetEntry>(
    viewBinding
) {
    override fun bind(item: SheetEntry?) {
        mDataBinding.dataPoint = item
        mDataBinding.cbSelected.visibility =  if (isSelectable){
            View.VISIBLE
        }else{
            View.GONE
        }
        metrics?.widthPixels?.let {
            val spacing = context.resources.getDimensionPixelSize(R.dimen.small_spacing)
            val holderWidth = it - spacing * 2
            if (mDataBinding.clContainer.layoutParams.width != holderWidth){
                mDataBinding.clContainer.layoutParams.width = holderWidth
            }
        }
    }

    fun lostFocus() {
        mDataBinding.root.clearFocus()
    }

    override var isSelectable: Boolean = false
}