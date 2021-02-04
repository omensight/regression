package com.alphemsoft.education.regression.ui.viewholder

import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.ItemSheetBinding
import com.alphemsoft.education.regression.ui.adapter.SheetPagingAdapter
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder
import com.alphemsoft.education.regression.ui.customview.QuestionDialog
import com.alphemsoft.education.regression.ui.fragment.SheetListFragmentDirections


class SheetItemViewHolder(
    viewBinding: ItemSheetBinding,
    navController: NavController? = null,
    private val onSheetItemActionListener: SheetPagingAdapter.OnSheetItemActionListener
) :
    BaseItemViewHolder<ItemSheetBinding, Sheet>(
        viewBinding,
        navController
    ) {
    override fun bind(item: Sheet?) {
        item?.let {
            mDataBinding.root.visibility = View.VISIBLE
            mDataBinding.sheet = item
            mDataBinding.llContainer.setOnClickListener {
                item.let {
                    val action =
                        SheetListFragmentDirections.actionDataSheetDetailFromSheetList(it.id)
                    navController?.navigate(action)
                }
            }
            val popupMenu = PopupMenu(context, mDataBinding.guidelineLeft)
            val removeItemQuestionDialog = QuestionDialog(
                context = context,
                title = R.string.proposal_sheet_remove,
                message = R.string.message_sheet_remove,
                positiveAction = {
                    onSheetItemActionListener.remove(item.id)
                }
            )
            popupMenu.inflate(R.menu.menu_sheet_item)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sheet_action_remove -> removeItemQuestionDialog.show()
                }
                true
            }
            mDataBinding.btMenu.setOnClickListener {
                popupMenu.show()
            }
        } ?: kotlin.run {
            mDataBinding.root.visibility = View.INVISIBLE
        }

    }

    override var isSelectable: Boolean = false
}