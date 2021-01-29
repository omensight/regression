package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseBottomSheetDialogFragment<VDB: ViewDataBinding, VM: ViewModel>(
    layoutId: Int,
    private val viewModelId: Int
) : BaseSimpleBottomSheetDialogFragment<VDB>(
    layoutId
) {

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = generateDataBinding(inflater, container)
        return dataBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun generateDataBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        val viewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VDB
        viewDataBinding.setVariable(viewModelId, viewModel)
        return viewDataBinding
    }
}