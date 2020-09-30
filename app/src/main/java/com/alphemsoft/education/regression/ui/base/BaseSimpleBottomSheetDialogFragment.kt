package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Job

abstract class BaseSimpleBottomSheetDialogFragment<VDB : ViewDataBinding> constructor(
    @LayoutRes protected val layoutId: Int,
): BottomSheetDialogFragment() {

    private val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected lateinit var dataBinding: VDB
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModelProvider = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory)
        dataBinding = generateDataBinding(inflater, container)
        return dataBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateDataBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        return DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VDB
    }
}