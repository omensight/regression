package com.alphemsoft.education.regression.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentShowExportResultBinding
import com.alphemsoft.education.regression.ui.base.BaseAppCompatActivity
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class AbstractExportResultSheetFragment :
    BaseBottomSheetDialogFragment<DialogFragmentShowExportResultBinding, DataSheetViewModel>(
        R.layout.dialog_fragment_show_export_result,
        BR.show_export_view_model
    )

class ExportResultSheetFragment : AbstractExportResultSheetFragment() {
    override val viewModel: DataSheetViewModel by activityViewModels()

    private lateinit var fileUri: Uri

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAds()
        setupPaths()
        setupUi()
    }

    private fun setupAds() {
        coroutineHandler.foregroundScope.launch {
            (activity as BaseAppCompatActivity<ViewDataBinding>)
                .nativeAdDispatcher
                .fetchAds()
                .firstOrNull { !it.isUsed }
                ?.unifiedNativeAd?.let {
                    dataBinding.adTemplateView.setNativeAd(it)
                }
        }
        coroutineHandler.foregroundScope.launch {
            dataBinding.adTemplateView.visibility = if (viewModel.hasPremiumSubscription()){
                View.GONE
            }else{
                View.VISIBLE
            }
        }
    }

    private fun setupPaths() {
        coroutineHandler.foregroundScope.launch {
            viewModel.exportUriLiveData.asFlow().collectLatest {
                it?.let { safeUri ->
                    fileUri = safeUri
                }
            }
        }

        dataBinding.btOk.setOnClickListener {
            requireActivity().findNavController(R.id.main_nav_host_fragment)
                .popBackStack()
        }
    }

    private fun setupUi() {
        dataBinding.btOpen.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val type = requireActivity().contentResolver.getType(fileUri)
            intent.type = type
            intent.data = fileUri
            requireActivity().startActivity(intent)
        }

        dataBinding.btShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val type = requireActivity().contentResolver.getType(fileUri)
            intent.type = type
            intent.putExtra(Intent.EXTRA_STREAM, fileUri)
            requireActivity().startActivity(intent)
        }

        coroutineHandler.foregroundScope.launch {
            viewModel.exportFileNameLiveData.asFlow().collectLatest {
                it?.let { fileName ->
                    dataBinding.tvDocumentLocation.text = "Documents/$fileName"
                }
            }
        }
    }

}