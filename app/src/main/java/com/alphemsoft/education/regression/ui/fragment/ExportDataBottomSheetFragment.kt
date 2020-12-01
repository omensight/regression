package com.alphemsoft.education.regression.ui.fragment

import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.CalendarContract.CalendarCache.URI
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentExportDataBinding
import com.alphemsoft.education.regression.dataexporter.CsvExportBehaviour
import com.alphemsoft.education.regression.dataexporter.DataExporter
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
    private var dataExporter: DataExporter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
        setupUi()
    }

    private fun setupUi() {
        dataBinding.btExport.setOnClickListener {
            val data = viewModel.dataEntries.value ?: return@setOnClickListener
            val fileName = dataBinding.etPath.text.toString()
            if (fileName.isEmpty()) {
                return@setOnClickListener
            }
            dataBinding.etPath.text?.toString()?.let { fileName ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$fileName.csv")
                    put(MediaStore.Files.FileColumns.MIME_TYPE, "text/*")
                    put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val csvExporter = CsvExportBehaviour(contentValues, requireActivity().contentResolver)
                dataExporter = DataExporter().apply {
                    exportBehaviour = csvExporter
                }
                if (dataExporter?.export(data) == true){
                    Toast.makeText(requireContext(), getString(R.string.document_saved), Toast.LENGTH_LONG).show()
                }
                dismiss()
            } ?: kotlin.run {

            }

        }

        dataBinding.btExport
    }
}