package com.alphemsoft.education.regression.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.ui.comparators.DiffUtilComparatorCallback

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val containsSubList: Boolean = false
) : RecyclerView.Adapter<VH>() {
    private val diffUtil = DiffUtilComparatorCallback<T>()
    protected val items: MutableList<T> = ArrayList()

    @CallSuper
    open fun addNewItems(newItems: List<T>) {
        val result = diffUtil.getResult(items, newItems)
        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(layoutInflater, parent, viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    override fun getItemCount(): Int {
        return items.size
    }
}