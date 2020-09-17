package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.FragmentSheetListBinding
import com.alphemsoft.education.regression.ui.adapter.SheetPagingAdapter
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class AbstractSheetListFragment :
    BaseFragment<FragmentSheetListBinding, DataSheetViewModel>(
        layoutId = R.layout.fragment_sheet_list,
        viewModelId = BR.sheet_list_view_model,
        menuResourceId = R.menu.menu_sheet_list,
        true,
    )


@AndroidEntryPoint
class SheetListFragment : AbstractSheetListFragment() {
    override val viewModel: DataSheetViewModel by viewModels()

    @Inject
    lateinit var sheetPagingAdapter: SheetPagingAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        coroutineHandler.foregroundScope.launch {
            viewModel.getAllSheets().collectLatest {
                sheetPagingAdapter.submitData(it)
            }
        }
        setupSheetList()
    }

    private fun setupSheetList() {
        val divider =
            com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerItemDecoration(18)
        dataBinding.rvSheetList.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(divider)
            adapter = sheetPagingAdapter
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sheet_list_new -> {
                findNavController().navigate(R.id.action_create_sheet)
                true
            }
            else -> false
        }
    }
}