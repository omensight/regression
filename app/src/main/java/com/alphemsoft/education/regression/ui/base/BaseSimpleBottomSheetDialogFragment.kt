package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Job

abstract class BaseSimpleBottomSheetDialogFragment<VDB : ViewDataBinding> constructor(
    @LayoutRes protected val layoutId: Int,
): BottomSheetDialogFragment() {

    private val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected lateinit var dataBinding: VDB
    protected lateinit var viewModelProvider: ViewModelProvider
    protected lateinit var mActivity: BaseAppCompatActivity<ViewDataBinding>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mActivity = requireActivity() as BaseAppCompatActivity<ViewDataBinding>
        viewModelProvider = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory)
        dataBinding = generateDataBinding(inflater, container)
        return dataBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    open fun generateDataBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        return DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VDB
    }
}