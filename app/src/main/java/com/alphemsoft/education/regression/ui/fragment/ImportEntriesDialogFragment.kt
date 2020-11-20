package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DialogFragmentImportDataBinding
import com.alphemsoft.education.regression.ui.adapter.ImportPairAdapter
import com.alphemsoft.education.regression.ui.base.BaseBottomSheetDialogFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import java.math.BigDecimal

class ImportEntriesDialogFragment: BaseBottomSheetDialogFragment<DialogFragmentImportDataBinding, DataSheetViewModel>(
    R.layout.dialog_fragment_import_data,
    BR.import_view_model
) {
    private val importPairAdapter: ImportPairAdapter = ImportPairAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setupAdapter()
    }

    private fun setupAdapter() {
        dataBinding.rvImportedEntries.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = importPairAdapter
        }
    }

    override val viewModel: DataSheetViewModel by viewModels()
}