package com.alphemsoft.education.regression.ui.viewholder

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.agog.mathdisplay.MTMathView
import com.alphemsoft.education.regression.data.model.secondary.Result
import com.alphemsoft.education.regression.databinding.ItemResultBinding
import com.alphemsoft.education.regression.ui.base.BaseItemViewHolder

class ResultItemViewHolder(mViewBinding: ItemResultBinding) :
    BaseItemViewHolder<ItemResultBinding, Result>(mViewBinding) {
    override fun bind(item: Result?) {
        item?.let {
            dataBinding.tvTitle.text = context.getString(item.title)
            val mathViewResult = dataBinding.mathViewResult
            mathViewResult.fontSize = 40f
            mathViewResult.textColor = Color.WHITE
            mathViewResult.textAlignment = MTMathView.MTTextAlignment.KMTTextAlignmentCenter
            mathViewResult.latex = item.formula
            mathViewResult.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            dataBinding.mathViewContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val resultWidth = mathViewResult.measuredWidth
            val containerWidth = dataBinding.mathViewContainer.measuredWidth
            mathViewResult.measuredHeight
            (mathViewResult.layoutParams as FrameLayout.LayoutParams).gravity = if (resultWidth < containerWidth) {
                Gravity.CENTER
            }else{
                Gravity.START
            }
        }
    }

    override var isSelectable: Boolean = false
}