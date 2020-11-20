package com.alphemsoft.education.regression.ui.comparators

import androidx.recyclerview.widget.DiffUtil
import com.alphemsoft.education.regression.data.model.SheetEntry

class DataEntryComparator: DiffUtilComparatorCallback<SheetEntry>() {
    override fun getCallback(
        oldList: List<SheetEntry>,
        newList: List<SheetEntry>
    ): DiffUtil.Callback {
        return object: DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id &&
                        oldList[oldItemPosition].tempHash == newList[newItemPosition].tempHash
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

        }
    }
}