package com.alphemsoft.education.regression.ui.viewholder

import androidx.core.content.ContextCompat
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.ItemImportPairBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import java.math.BigDecimal

class ImportPairViewHolder(mViewBinding: ItemImportPairBinding) : BaseItemViewHolder<ItemImportPairBinding, Pair<BigDecimal, BigDecimal>>(
    mViewBinding
) {
    override fun bind(item: Pair<BigDecimal, BigDecimal>?) {
        mDataBinding.x = item?.first
        mDataBinding.y = item?.second
        val currentPos = absoluteAdapterPosition
        val colorResource = if (currentPos % 2 == 0){
            R.color.color_background_white
        }else{
            R.color.color_background_dark
        }
        val colorBackground = ContextCompat.getColor(context, colorResource)
        mDataBinding.container.setBackgroundColor(colorBackground)
    }

    override var isSelectable: Boolean = false
}