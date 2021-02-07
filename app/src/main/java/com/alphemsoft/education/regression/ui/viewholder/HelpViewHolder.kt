package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.alphemsoft.education.regression.data.model.secondary.Help
import com.alphemsoft.education.regression.databinding.ItemHelpBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class HelpViewHolder(mDataBinding: ItemHelpBinding, override var isSelectable: Boolean = false) :
    BaseItemViewHolder<ItemHelpBinding, Help>(mDataBinding) {
    override fun bind(item: Help?) {
        item?.let { safeItem ->
            mDataBinding.apply {
                ivIcon.setImageResource(safeItem.icon)
                tvTitle.setText(safeItem.title)
                safeItem.description?.let {
                    tvDescription.setText(it)
                    tvDescription.visibility = View.VISIBLE
                }?: run {
                    tvDescription.visibility = View.GONE
                }
            }
        }
    }
}