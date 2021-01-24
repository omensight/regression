package com.alphemsoft.education.regression.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentExportDataBinding
import com.alphemsoft.education.regression.dataexporter.DataExportHelper
import com.alphemsoft.education.regression.dataexporter.ExportBehaviour
import com.alphemsoft.education.regression.dataexporter.FileData
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                if (isChecked){
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
        dataBinding.btExport.setOnClickListener {
            val data = viewModel.dataEntries.value ?: return@setOnClickListener
            val selectedFormat = viewModel.exportFormatLiveData.value
            val type = FileData.Format.values().first { it.extension == selectedFormat}
            val fileName = dataBinding.etPath.text.toString()
            if (fileName.isEmpty()) {
                return@setOnClickListener
            }
            dataBinding.etPath.text?.toString()?.let { fileName ->
                dataExportHelper = DataExportHelper().apply {
                    exportBehaviour = ExportBehaviour.Builder(
                        requireContext(),
                        when(type){
                            FileData.Format.CSV -> FileData.Csv(fileName)
                            FileData.Format.XLSX -> FileData.Excel(fileName)
                        }
                    ).build()
                }
                if (dataExportHelper?.export(data) == true) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.document_saved),
                        Toast.LENGTH_LONG
                    ).show()
                }
                dismiss()
            } ?: kotlin.run {

            }

        }
        dataBinding.lifecycleOwner = this
        dataBinding.btCancel.setOnClickListener {
            dismiss()
        }
    }
}