package com.alphemsoft.education.regression.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import kotlinx.coroutines.Job

abstract class BaseDialogFragment<VDB : ViewDataBinding> constructor(
    @LayoutRes protected val layoutId: Int,
): DialogFragment() {

    private val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected lateinit var dataBinding: VDB
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var activity: BaseAppCompatActivity<ViewDataBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as BaseAppCompatActivity<ViewDataBinding>
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

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