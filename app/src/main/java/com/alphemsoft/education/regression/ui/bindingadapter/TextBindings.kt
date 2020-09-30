package com.alphemsoft.education.regression.ui.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetType
import com.alphemsoft.education.regression.extensions.getSimpleDate
import com.google.android.material.textview.MaterialTextView
import java.math.BigDecimal
import java.text.DecimalFormat
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

        @JvmStatic
        @BindingAdapter("app:setBigDecimal")
        fun TextView.setBigDecimal(bigDecimal: BigDecimal?){
            val value = try {
                val pattern = "0.${"#".repeat(1000)}"
                val decimalFormatter = DecimalFormat(pattern)
                decimalFormatter.format(bigDecimal) ?: ""
            } catch (e: IllegalArgumentException) {
                ""
            }
            this.text = value
        }
    }
}