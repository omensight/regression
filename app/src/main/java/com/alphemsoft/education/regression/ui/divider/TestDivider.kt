package com.alphemsoft.education.regression.ui.divider

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.alphemsoft.education.regression.R
import kotlin.math.max
import kotlin.math.min

class TestDivider(private val radius: Float) : ItemDecoration() {
    private val defaultRectToClip: RectF
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        val rectToClip = getRectToClip(parent)

        // has no items with ViewType == `R.layout.item_image`
//        if (rectToClip == defaultRectToClip) {
//            return
//        }
        for (child in parent.children) {
            val rectToClip = RectF()
            rectToClip.left = child.left.toFloat()
            rectToClip.top = child.top.toFloat()
            rectToClip.right = child.right.toFloat()
            rectToClip.bottom = child.bottom.toFloat()
            val path = Path()
            path.addRoundRect(rectToClip, radius, radius, Path.Direction.CW)
            canvas.clipRect(rectToClip)

        }

    }

    private fun getRectToClip(parent: RecyclerView): RectF {
        val rectToClip = RectF(defaultRectToClip)
        val childRect = Rect()
        for (i in 0 until parent.childCount) {

            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, childRect)
            rectToClip.left = min(rectToClip.left, childRect.left.toFloat())
            rectToClip.top = min(rectToClip.top, childRect.top.toFloat())
            rectToClip.right = max(rectToClip.right, childRect.right.toFloat())
            rectToClip.bottom = max(rectToClip.bottom, childRect.bottom.toFloat())
        }
        return rectToClip
    }

    private fun isImage(parent: RecyclerView, viewPosition: Int): Boolean {
        val adapter = parent.adapter
        val viewType = adapter!!.getItemViewType(viewPosition)
        return viewType == 0
    }

    init {
        defaultRectToClip = RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0.0f, 0f)
    }
}