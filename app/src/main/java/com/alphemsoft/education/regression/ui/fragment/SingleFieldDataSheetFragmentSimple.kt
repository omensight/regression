package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.BottomSheetFragmentSingleFieldBinding
import com.alphemsoft.education.regression.ui.SimpleFieldModelUi
import com.alphemsoft.education.regression.ui.base.BaseSimpleBottomSheetDialogFragment

class SingleFieldDataSheetFragmentSimple:BaseSimpleBottomSheetDialogFragment<BottomSheetFragmentSingleFieldBinding>(
    R.layout.bottom_sheet_fragment_single_field
) {

    private lateinit var listener: SimpleFieldListener<Int>
    private var modelUiSettled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.App_Theme_BottomSheetDialogFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        dataBinding.btOk.setOnClickListener {
            val field = dataBinding.etField.text.toString().toIntOrNull()
            field?.let {
                listener.onFieldFilled(it)
                dismiss()
            }?:run {
               dataBinding.tilField.error = getString(R.string.error_invalid)
            }
        }

        dataBinding.btCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setModelUi(simpleFieldModelUi: SimpleFieldModelUi){
        lifecycleScope.launchWhenResumed {
            dataBinding.simpleFieldModelUi = simpleFieldModelUi
            modelUiSettled = true
        }
    }

    fun setSimpleFieldListener(simpleFieldListener: SimpleFieldListener<Int>){
        this.listener = simpleFieldListener
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }
}