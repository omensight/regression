package com.alphemsoft.education.regression.ui.bindingadapter

import androidx.databinding.BindingAdapter
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetType
import com.alphemsoft.education.regression.extensions.getSimpleDate
import com.google.android.material.textview.MaterialTextView
import java.util.*

class TextBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("app:createdOn")
        fun bindDate(textView: MaterialTextView, date: Date) {
            textView.text = date.getSimpleDate()
        }

        @JvmStatic
        @BindingAdapter("app:bindSheetType")
        fun bindSheetType(textView: MaterialTextView, sheetType: SheetType) {
            val context = textView.context
            textView.text =
                context.resources.getStringArray(R.array.regression_types)[sheetType.type][0].toString()
        }
    }
}