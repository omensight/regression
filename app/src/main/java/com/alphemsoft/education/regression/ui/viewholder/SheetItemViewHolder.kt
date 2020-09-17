package com.alphemsoft.education.regression.ui.viewholder

import android.view.View
import androidx.navigation.NavController
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.ItemSheetBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.fragment.CreateSheetFragmentDirections


class SheetItemViewHolder(viewBinding: ItemSheetBinding, navController: NavController? = null)  :
    BaseItemViewHolder<ItemSheetBinding, Sheet>(
        viewBinding,
        navController
    ) {
    override fun bind(item: Sheet?) {
        item?.let {
            mViewBinding.root.visibility = View.VISIBLE
            mViewBinding.sheet = item
        }?: kotlin.run {
            mViewBinding.root.visibility = View.INVISIBLE
        }
        mViewBinding.viewBackground.setOnClickListener {
            item?.let {
                val action = CreateSheetFragmentDirections.actionDataSheetDetail(it.id)
                navController?.navigate(action)
            }

        }
    }

    override var isSelectable: Boolean = false
}