package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetEntry
import com.alphemsoft.education.regression.data.regression.Regression
import com.alphemsoft.education.regression.data.regression.RegressionFactory
import com.alphemsoft.education.regression.databinding.DialogFragmentExportReportBinding
import com.alphemsoft.education.regression.dataexporter.SheetExporter
import com.alphemsoft.education.regression.extensions.getSimpleFormatted
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.SaveReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

abstract class AbstractSaveReportBottomSheetFragment
    : BaseBottomSheetDialogFragment<DialogFragmentExportReportBinding, SaveReportViewModel>(
    R.layout.dialog_fragment_export_report,
    BR.dialog_fragment_export_report_viewmodel
)

@AndroidEntryPoint
class SaveReportBottomSheetFragment : AbstractSaveReportBottomSheetFragment() {
    override val viewModel: SaveReportViewModel by viewModels()
    private val args: SaveReportBottomSheetFragmentArgs by navArgs()
    private lateinit var sheetExporter: SheetExporter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
        sheetExporter = SheetExporter(requireContext())
    }

    private fun setupUi() {
        dataBinding.lifecycleOwner = this
        coroutineHandler.foregroundScope.launch {
            val date = Date().getSimpleFormatted()
            viewModel.initialize(args.sheetId, date)
        }

        dataBinding.btCancel.setOnClickListener {
            dismiss()
        }

        dataBinding.btExport.setOnClickListener {
            coroutineHandler.backgroundScope.launch {
                coroutineHandler.foregroundScope.launch {
                    dataBinding.btExport.text = getString(R.string.export_saving)
                }
                viewModel.fileNameLiveData.value?.let { fileName ->
                    sheetExporter.initialize(fileName)?.let { uri ->
                        viewModel.getSheet(args.sheetId)?.let { sheet ->
                            val regression: Regression =
                                RegressionFactory.generateRegression(sheet.type)
                            val entries: List<SheetEntry> = viewModel.getSheetEntries(args.sheetId)
                            if (entries.isNotEmpty()) {
                                regression.setData(entries)
                                val results = regression.getResults(5)
                                sheetExporter.export(sheet)
                                sheetExporter.export(entries)
                                sheetExporter.export(results)
                                val saved = sheetExporter.save()
                                if (saved) {
                                    coroutineHandler.foregroundScope.launch {
                                        val destination =
                                            SaveReportBottomSheetFragmentDirections.actionDestinationExportCompleteReportToDestinationExportResult(
                                                uri,
                                                fileName
                                            )
                                        val controller =
                                            requireActivity().findNavController(R.id.main_nav_host_fragment)
                                        controller.navigate(destination)
                                    }

                                }else{
                                    coroutineHandler.foregroundScope.launch {
                                        Toast.makeText(requireContext(), getString(R.string.export_problem_saving_error_22), Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}