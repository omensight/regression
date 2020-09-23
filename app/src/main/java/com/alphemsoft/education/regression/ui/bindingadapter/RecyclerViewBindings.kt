package com.alphemsoft.education.regression.ui.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.data.model.DataEntry
import com.alphemsoft.education.regression.ui.adapter.DataEntryAdapter

class RecyclerViewBindings {
    companion object{
        @JvmStatic
        @BindingAdapter(value = ["dataEntries"])
        fun RecyclerView.addDataEntries(entries: List<DataEntry>){
            adapter?: kotlin.run {
                adapter = DataEntryAdapter()
            }
            (adapter as DataEntryAdapter).addNewItems(entries)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        }
    }
}