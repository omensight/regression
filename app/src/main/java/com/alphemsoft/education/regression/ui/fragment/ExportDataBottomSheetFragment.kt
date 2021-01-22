package com.alphemsoft.education.regression.ui.fragment

import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentExportDataBinding
import com.alphemsoft.education.regression.dataexporter.DataExportHelper
import com.alphemsoft.education.regression.dataexporter.ExportBehaviour
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
        isCancelable = false
        setupUi()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.endDataExport()
    }

    private fun setupUi() {
        dataBinding.btExport.setOnClickListener {
            val data = viewModel.dataEntries.value ?: return@setOnClickListener
            val fileName = dataBinding.etPath.text.toString()
            if (fileName.isEmpty()) {
                return@setOnClickListener
            }
            dataBinding.etPath.text?.toString()?.let { fileName ->
                dataExportHelper = DataExportHelper().apply {
                    exportBehaviour = ExportBehaviour.Builder(
                        requireContext(),
                        DataExportHelper.FileData.Csv(fileName)
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