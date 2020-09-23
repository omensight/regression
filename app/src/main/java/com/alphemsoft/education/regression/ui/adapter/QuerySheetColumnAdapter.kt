package com.alphemsoft.education.regression.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alphemsoft.education.regression.data.model.QuerySheetDataColumn
import com.alphemsoft.education.regression.databinding.ItemQuerySheetColumnBinding
import com.alphemsoft.education.regression.ui.base.BaseAdapter
import com.alphemsoft.education.regression.ui.viewholder.QuerySheetColumnViewHolder
import javax.inject.Inject

class QuerySheetColumnAdapter @Inject constructor() : BaseAdapter<QuerySheetDataColumn, QuerySheetColumnViewHolder>() {

    lateinit var displayMetrics: DisplayMetrics

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): QuerySheetColumnViewHolder {
        val binding =
            ItemQuerySheetColumnBinding.inflate(inflater, parent, false)
        return QuerySheetColumnViewHolder(binding, displayMetrics)
    }

    fun addNewItems(newItems: List<QuerySheetDataColumn>, displayMetrics: DisplayMetrics) {
        this.displayMetrics = displayMetrics
        super.addNewItems(newItems)
    }

    override fun onBindViewHolder(holder: QuerySheetColumnViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun addNewItems(newItems: List<QuerySheetDataColumn>) {
        super.addNewItems(newItems)
//        notifyDataSetChanged()
    }

}