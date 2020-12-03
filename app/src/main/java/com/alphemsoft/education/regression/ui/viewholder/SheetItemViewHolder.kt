package com.alphemsoft.education.regression.ui.viewholder

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import com.alphemsoft.education.regression.R
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
        val backgroundColor = if (absoluteAdapterPosition % 2 == 0){
            ContextCompat.getColor(context, R.color.color_data_even_position)
        } else{
            ContextCompat.getColor(context, R.color.color_data_odd_position)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
            mDataBinding.viewBackground.background.setColorFilter(backgroundColor, PorterDuff.Mode.MULTIPLY)
        }else{
            mDataBinding.viewBackground.background.colorFilter = BlendModeColorFilter(backgroundColor, BlendMode.MULTIPLY)
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