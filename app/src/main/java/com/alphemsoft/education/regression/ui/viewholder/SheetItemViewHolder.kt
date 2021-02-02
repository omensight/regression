package com.alphemsoft.education.regression.ui.viewholder

import android.graphics.*
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.ItemSheetBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.fragment.SheetListFragmentDirections


class SheetItemViewHolder(viewBinding: ItemSheetBinding, navController: NavController? = null)  :
    BaseItemViewHolder<ItemSheetBinding, Sheet>(
        viewBinding,
        navController
    ) {
    override fun bind(item: Sheet?) {
        item?.let {
            mDataBinding.root.visibility = View.VISIBLE
            mDataBinding.sheet = item
        }?: kotlin.run {
            mDataBinding.root.visibility = View.INVISIBLE
        }
        val backgroundColorResource = if (absoluteAdapterPosition % 2 == 0){
            R.color.color_data_even_position
        } else{
            R.color.color_data_odd_position
        }
        mDataBinding.llContainer.setOnClickListener {
            item?.let {
                val action = SheetListFragmentDirections.actionDataSheetDetailFromSheetList(it.id)
                navController?.navigate(action)
            }
        }
    }

    override var isSelectable: Boolean = false
}