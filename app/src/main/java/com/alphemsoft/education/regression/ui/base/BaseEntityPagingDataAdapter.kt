package com.alphemsoft.education.regression.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.ui.comparators.DbEntityComparatorItemCallback

abstract class BaseEntityPagingDataAdapter<T: Any, VH : RecyclerView.ViewHolder>(
    itemItemCallback: DbEntityComparatorItemCallback<T>
) :
    PagingDataAdapter<T, VH>(itemItemCallback) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(layoutInflater, parent,viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater,parent: ViewGroup, viewType: Int): VH
}