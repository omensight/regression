package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseBottomSheetDialogFragment<VDB: ViewDataBinding, VM: ViewModel>(
    layoutId: Int,
    private val viewModelId: Int
) : BaseSimpleBottomSheetDialogFragment<VDB>(
    layoutId
) {

    protected abstract val viewModel: VM

    protected lateinit var activity: BaseAppCompatActivity
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = requireActivity() as BaseAppCompatActivity
        viewModelProvider = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory)
        dataBinding = generateDataBinding(inflater, container)
        return dataBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateDataBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        val viewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VDB
        viewDataBinding.setVariable(viewModelId, viewModel)
        return viewDataBinding
    }
}