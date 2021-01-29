package com.alphemsoft.education.regression.ui.fragment

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentExportDataBinding
import com.alphemsoft.education.regression.dataexporter.DataExportHelper
import com.alphemsoft.education.regression.dataexporter.exportbehaviour.ExportBehaviour
import com.alphemsoft.education.regression.dataexporter.FileData
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseExportDataBottomSheetFragment :
    BaseBottomSheetDialogFragment<DialogFragmentExportDataBinding, DataSheetViewModel>(
        layoutId = R.layout.dialog_fragment_export_data,
        viewModelId = BR.dialog_fragment_export_viewmodel
    )

@AndroidEntryPoint
class ExportDataBottomSheetFragment : BaseExportDataBottomSheetFragment() {
    override val viewModel: DataSheetViewModel by activityViewModels()
    private var dataExportHelper: DataExportHelper? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = this
        isCancelable = false
        setupSupportedFormatList()
        setupUi()
    }

    private fun setupSupportedFormatList() {
        val supportedFormats = FileData.getSupportedFormats()
        val rgFormatOptions = dataBinding.rgFormatOptions
        supportedFormats.forEachIndexed { index, text ->
            val radioButtonFormatOption = RadioButton(context)
            radioButtonFormatOption.isAllCaps = true
            radioButtonFormatOption.text = text
            rgFormatOptions.addView(radioButtonFormatOption)
            radioButtonFormatOption.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    viewModel.exportFormatLiveData.value = supportedFormats[index]
                }
            }
        }
        (rgFormatOptions.getChildAt(0) as RadioButton).isChecked = true
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.endDataExport()
    }

    private fun setupUi() {
        coroutineHandler.foregroundScope.launch {
            viewModel.exportSaving.asFlow().collectLatest {
                it?.let {saving->
                    if (saving){
                        dataBinding.btExport.apply {
                            text = getString(R.string.export_saving)
                            isEnabled = false
                        }
                    }else{
                        dataBinding.btExport.apply {
                            text = getString(R.string.export)
                            isEnabled = true
                        }
                    }
                }
            }
        }
        dataBinding.btExport.setOnClickListener {
            coroutineHandler.backgroundScope.launch {
                val data = viewModel.dataEntries.value ?: return@launch
                viewModel.exportSaving.postValue(true)
                val selectedFormat = viewModel.exportFormatLiveData.value
                val type = FileData.Format.values().first { it.extension == selectedFormat }
                val fileName = dataBinding.etPath.text.toString()
                if (fileName.isEmpty()) {
                    viewModel.exportSaving.postValue(false)
                    return@launch
                }
                dataBinding.etPath.text?.toString()?.let { fileName ->
                    dataExportHelper = DataExportHelper().apply {
                        exportBehaviour = ExportBehaviour.Builder(
                            requireContext(),
                            when (type) {
                                FileData.Format.CSV -> FileData.Csv(fileName)
                                FileData.Format.XLSX -> FileData.Excel(fileName)
                            }
                        ).build()
                    }
                    val uri = dataExportHelper?.uri
                    if (dataExportHelper?.export(data) == true && uri != null) {
                        coroutineHandler.foregroundScope.launch {
                            val destination = ExportDataBottomSheetFragmentDirections.actionDestinationExportDataToDestinationExportResult(uri,fileName)
                            requireActivity().findNavController(R.id.main_nav_host_fragment)
                                .navigate(destination)
                        }
                    }else{
                        coroutineHandler.foregroundScope.launch {
                            Toast.makeText(requireContext(), getString(R.string.export_problem_saving_error_21), Toast.LENGTH_SHORT).show()
                        }
                    }
                    viewModel.exportSaving.postValue(false)
                    dismiss()
                }
            }

        }
        dataBinding.lifecycleOwner = this
        dataBinding.btCancel.setOnClickListener {
            dismiss()
        }
    }
}