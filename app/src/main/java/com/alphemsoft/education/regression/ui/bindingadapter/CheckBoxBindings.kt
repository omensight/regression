package com.alphemsoft.education.regression.ui.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.checkbox.MaterialCheckBox

class CheckBoxBindings {
    companion object{

        @BindingAdapter("itemSelectedAttrChanged")
        @JvmStatic
        fun itemCheckedAttrChanged(checkBox: MaterialCheckBox, inverseBindingListener: InverseBindingListener){
            checkBox.setOnCheckedChangeListener { _, _ ->
                inverseBindingListener.onChange()
            }
        }

        @BindingAdapter("itemSelected")
        @JvmStatic
        fun setItemChecked(checkBox: MaterialCheckBox, checked: Boolean){
            checkBox.isChecked = checked
        }

        @InverseBindingAdapter(attribute = "itemSelected")
        @JvmStatic
        fun getBooleanFromCheckBox(checkBox: MaterialCheckBox): Boolean{
            return checkBox.isChecked
        }
    }
}
