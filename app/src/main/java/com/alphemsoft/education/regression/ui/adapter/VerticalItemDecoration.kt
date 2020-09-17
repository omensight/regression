package com.alphemsoft.education.regression.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.getChildAdapterPosition(view)
        outRect.top = 24
        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun toString(): String {
        return super.toString()
    }
}