package com.alphemsoft.education.regression.ui.viewholder

import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.databinding.ItemDataEntryBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class DataEntryItemViewHolder(viewBinding: ItemDataEntryBinding) : BaseItemViewHolder<ItemDataEntryBinding, DataEntry>(
    viewBinding
) {
    override var isSelectable: Boolean = false

    override fun bind(item: DataEntry?) {
        dataBinding.dataPoint = item
    }
}