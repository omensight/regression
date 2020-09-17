package com.alphemsoft.education.regression.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.ui.comparators.DiffUtilComparatorCallback

abstract class BaseEntityAdapter<T: Any, VH : RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {
    val diffUtil = DiffUtilComparatorCallback<T>()
    protected val items: MutableList<T> = ArrayList()
    fun addNewItems(newItems: List<T>){
        val result = diffUtil.getResult(items, newItems)
        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(layoutInflater, parent,viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater,parent: ViewGroup, viewType: Int): VH

    override fun getItemCount(): Int {
        return items.size
    }
}