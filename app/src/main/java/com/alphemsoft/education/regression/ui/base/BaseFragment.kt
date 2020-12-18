package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.activity.NativeAdDispatcher
import kotlinx.coroutines.Job

abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel>(
    @LayoutRes protected val layoutId: Int,
    private val viewModelId: Int,
    @MenuRes val menuResourceId: Int? = null
) : Fragment() {
    private val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected abstract val viewModel: VM
    protected lateinit var dataBinding: VDB
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var mActivity: BaseAppCompatActivity<ViewDataBinding>
    protected lateinit var nativeAdDispatcher: NativeAdDispatcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuResourceId != null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mActivity = requireActivity() as BaseAppCompatActivity<ViewDataBinding>
        nativeAdDispatcher = mActivity.nativeAdDispatcher
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuResourceId?.let { resourceId ->
            inflater.inflate(resourceId, menu)
        } ?: run {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }
}