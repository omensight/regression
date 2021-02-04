package com.alphemsoft.education.regression.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.ItemSheetBinding
import com.alphemsoft.education.regression.ui.base.BaseEntityPagingDataAdapter
import com.alphemsoft.education.regression.ui.comparators.DbEntityComparatorItemCallback
import com.alphemsoft.education.regression.ui.viewholder.SheetItemViewHolder

class SheetPagingAdapter constructor(
    private val navController: NavController,
    private val onSheetItemActionListener: OnSheetItemActionListener

) :
    BaseEntityPagingDataAdapter<Sheet, SheetItemViewHolder>(
        DbEntityComparatorItemCallback()
    ) {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): SheetItemViewHolder {
        val binding = ItemSheetBinding.inflate(inflater, parent, false)
        return SheetItemViewHolder(binding, navController, onSheetItemActionListener)
    }

    override fun onBindViewHolder(holder: SheetItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnSheetItemActionListener{
        fun remove(sheetId: Long)
    }

}