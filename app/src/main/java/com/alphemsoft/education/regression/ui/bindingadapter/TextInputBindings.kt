package com.alphemsoft.education.regression.ui.bindingadapter

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.alphemsoft.education.regression.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal
import java.text.DecimalFormat

class TextInputBindings {
    companion object {

        @BindingAdapter("app:emptyError")
        @JvmStatic
        fun errorAdapter(child: TextInputEditText, txt: String?) {
            val parent = child.parent.parent
            require(parent is TextInputLayout)
            child.doOnTextChanged { text, _, _, _ ->
                parent.error = if (text.isNullOrEmpty()) {
                    parent.context.getText(R.string.text_error_empty_field)
                } else {
                    null
                }
            }
        }

        @BindingAdapter("realValueAttrChanged")
        @JvmStatic
        fun setListener(editText: TextInputEditText, listener: InverseBindingListener?) {
            if (listener != null) {
                editText.doOnTextChanged { _, _, _, _ ->
                    listener.onChange()
                }
            }
        }

        @BindingAdapter("realValue")
        @JvmStatic
        fun setDoubleValue(editText: TextInputEditText, bigDecimal: BigDecimal?) {
            val value = try {
                val pattern = "0.${"#".repeat(1000)}"
                val decimalFormatter = DecimalFormat(pattern)
                decimalFormatter.format(bigDecimal) ?: ""
            } catch (e: IllegalArgumentException) {
                ""
            }
            editText.setText(value)
            val length = editText.text?.length?:0
            if (length > 0){
                editText.setSelection(length)
            }
        }

        @InverseBindingAdapter(attribute = "realValue")
        @JvmStatic
        fun getDoubleFromEditText(editText: TextInputEditText): BigDecimal? {
            val text = editText.text.toString()
            return try {
                BigDecimal(text)
            } catch (e: NumberFormatException) {
                null
            }
        }
    }
}