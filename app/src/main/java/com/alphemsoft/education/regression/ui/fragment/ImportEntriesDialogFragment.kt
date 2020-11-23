package com.alphemsoft.education.regression.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentImportDataBinding
import com.alphemsoft.education.regression.ui.adapter.ImportPairAdapter
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_fragment_import_data.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseImportEntriesDialogFragment: BaseBottomSheetDialogFragment<DialogFragmentImportDataBinding, DataSheetViewModel>(
    R.layout.dialog_fragment_import_data,
    BR.import_view_model
)

@AndroidEntryPoint
class ImportEntriesDialogFragment: BaseImportEntriesDialogFragment() {
    private lateinit var importPairAdapter: ImportPairAdapter

    override val viewModel: DataSheetViewModel by activityViewModels()

    private val navArgs: ImportEntriesDialogFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        importPairAdapter = ImportPairAdapter()
        setupAdapter()
        setupUi()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setupUi() {

        dataBinding.tvImportMessage.text = if (navArgs.errorCount > 0){
            getString(R.string.import_data_message_with_errors).format(navArgs.errorCount)
        }else{
            getString(R.string.import_data_message)
        }
        
        dataBinding.btImport.setOnClickListener {
            viewModel.addImportedDataToDataList()
            dismiss()
        }

        dataBinding.btCancel.setOnClickListener {
            viewModel.emptyImportedData()
            dismiss()
        }
    }

    private fun setupAdapter() {
        dataBinding.rvImportedEntries.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = importPairAdapter
        }
        viewModel.importedEntries.observe(requireActivity(), {
            importPairAdapter.addNewItems(it)
        })
    }
}