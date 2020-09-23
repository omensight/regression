package com.alphemsoft.education.regression.ui.viewholder

import android.util.DisplayMetrics
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.databinding.ItemQuerySheetColumnBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class QuerySheetColumnViewHolder(
    mViewBinding: ItemQuerySheetColumnBinding,
    private val displayMetrics: DisplayMetrics,
) : BaseItemViewHolder<ItemQuerySheetColumnBinding, QuerySheetDataColumn>(
    mViewBinding) {
    override var isSelectable: Boolean = false
    override fun bind(item: QuerySheetDataColumn?) {
        dataBinding.querySheetDataColumn = item
        dataBinding.root.layoutParams.width = displayMetrics.widthPixels/2
    }


}