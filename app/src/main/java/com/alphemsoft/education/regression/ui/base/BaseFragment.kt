package com.alphemsoft.education.regression.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.data.model.AdEntity
import com.alphemsoft.education.regression.ui.OnAdLoadedListener
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Job

abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel>(
    @LayoutRes protected val layoutId: Int,
    private val viewModelId: Int,
    @MenuRes val menuResourceId: Int? = null,
    private val supportsNativeAds: Boolean = false
) : Fragment(), OnAdLoadedListener {
    private val job = Job()
    protected val coroutineHandler = CoroutineHandler(job)
    protected abstract val viewModel: VM
    protected lateinit var dataBinding: VDB
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var activity: BaseAppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(menuResourceId != null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = requireActivity() as BaseAppCompatActivity
        viewModelProvider = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory)
        dataBinding = generateDataBinding(inflater, container)
        if(supportsNativeAds){
            activity.addAdLoadListener(this)
        }
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

    fun getUnifiedNativeAds(): List<UnifiedNativeAd> {
        return activity.getUnifiedNativeAds()
    }


    @CallSuper
    override fun onAdsLoaded(unifiedNativeAds: List<AdEntity>, adsChanged: Boolean) {
        require(supportsNativeAds){"Load native ads not supported by fragment"}
    }
}