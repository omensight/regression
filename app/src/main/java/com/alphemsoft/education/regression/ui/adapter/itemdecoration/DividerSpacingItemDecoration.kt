package com.alphemsoft.education.regression.ui.adapter.itemdecoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

class DividerSpacingItemDecoration(private val spacing: Int): RecyclerView.ItemDecoration()
{

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val topOffset = when(position){
            0-> spacing
            else -> 0
        }
        val bottomOffset = spacing
        val sideOffset = spacing
        if (position == NO_POSITION){
            return
        }else{
            outRect.set(sideOffset,topOffset,sideOffset,bottomOffset)
        }

    }
}