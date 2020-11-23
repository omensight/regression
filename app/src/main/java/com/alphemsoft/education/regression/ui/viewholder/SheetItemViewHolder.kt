package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import androidx.navigation.NavController
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.ItemSheetBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.fragment.CreateSheetFragmentDirections
import com.alphemsoft.education.regression.ui.fragment.DataSheetFragmentDirections
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
        mDataBinding.viewBackground.setOnClickListener {
            item?.let {
                val action = SheetListFragmentDirections.actionDataSheetDetailFromSheetList(it.id)
                navController?.navigate(action)
            }

        }
    }

    override var isSelectable: Boolean = false
}