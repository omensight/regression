package com.alphemsoft.education.regression.ui.viewholder

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.databinding.ItemDataPointBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class DataPairViewHolder(
    viewBinding: ItemDataPointBinding,
    private val metrics: DisplayMetrics? = null
) : BaseItemViewHolder<ItemDataPointBinding, SheetEntry>(
    viewBinding
) {
    override fun bind(item: SheetEntry?) {
        mDataBinding.dataPoint = item
        mDataBinding.position = absoluteAdapterPosition + 1
        val backgroundColor =
            if (absoluteAdapterPosition % 2 == 0)
                ContextCompat.getColor(context, R.color.color_data_even_position)
            else
                ContextCompat.getColor(context, R.color.color_data_odd_position)
        mDataBinding.clContainer.background.setTint(backgroundColor)
        mDataBinding.cbSelected.visibility = if (isSelectable) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun lostFocus() {
        mDataBinding.root.clearFocus()
    }

    override var isSelectable: Boolean = false
}