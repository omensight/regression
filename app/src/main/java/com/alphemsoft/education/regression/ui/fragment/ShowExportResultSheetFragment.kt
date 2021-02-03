package com.alphemsoft.education.regression.ui.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentShowExportResultBinding
import com.alphemsoft.education.regression.ui.base.BaseAppCompatActivity
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.ShowExportResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class AbstractShowExportResultSheetFragment :
    BaseBottomSheetDialogFragment<DialogFragmentShowExportResultBinding, ShowExportResultViewModel>(
        R.layout.dialog_fragment_show_export_result,
        BR.show_export_view_model
    )

@AndroidEntryPoint
class ShowExportResultSheetFragment : AbstractShowExportResultSheetFragment() {
    override val viewModel: ShowExportResultViewModel by viewModels()
    private val args: ShowExportResultSheetFragmentArgs by navArgs()
    private lateinit var fileUri: Uri

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = this
        getExtras()
        setupPaths()
        setupUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setupAds()
    }

    private fun getExtras() {
        viewModel.initialize(args.fileName, args.fileUri)
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
            dataBinding.adTemplateView.visibility = if (viewModel.hasPremiumSubscription()) {
                View.GONE
            } else {
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
            dismiss()
        }
    }

    private fun setupUi() {
        dataBinding.btOpen.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val type = requireActivity().contentResolver.getType(fileUri)
                intent.type = type
                intent.data = fileUri
                requireActivity().startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.export_error_no_application_to_open_file),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dataBinding.btShare.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                val type = requireActivity().contentResolver.getType(fileUri)
                intent.type = type
                intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                requireActivity().startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.export_error_no_application_to_open_file),
                    Toast.LENGTH_SHORT
                ).show()
            }
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