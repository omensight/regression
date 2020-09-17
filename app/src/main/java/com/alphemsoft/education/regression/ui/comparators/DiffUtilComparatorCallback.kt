package com.alphemsoft.education.regression.ui.comparators

import androidx.recyclerview.widget.DiffUtil

class DiffUtilComparatorCallback<T: Any>() {
    private fun getCallback(oldList: List<T>, newList: List<T>) = object : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun getResult(oldList: List<T>, newList: List<T>): DiffUtil.DiffResult{
        val callback = getCallback(oldList, newList)
        return DiffUtil.calculateDiff(callback)
    }
}