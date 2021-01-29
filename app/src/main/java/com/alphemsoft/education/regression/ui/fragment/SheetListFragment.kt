package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.Sheet
import com.alphemsoft.education.regression.databinding.FragmentSheetListBinding
import com.alphemsoft.education.regression.ui.adapter.SheetPagingAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerSpacingItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.ui.divider.TestDivider
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
    )


@AndroidEntryPoint
class SheetListFragment : AbstractSheetListFragment() {
    override val viewModel: DataSheetViewModel by viewModels()

    lateinit var sheetPagingAdapter: SheetPagingAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
        setupSheetList()
        coroutineHandler.backgroundScope.launch {
            viewModel.migrateLegacyData()
        }
    }

    private fun setupUi() {
        dataBinding.layoutEmptyViewSheets.apply {
            tvEmptyViewDescription.text = getString(R.string.empty_message_no_sheets)
            ivEmptyViewIcon.setImageResource(R.drawable.ic_empty_view_sheets)
        }
    }

    private fun setupSheetList() {

        sheetPagingAdapter = SheetPagingAdapter(findNavController())
        coroutineHandler.foregroundScope.launch {
            viewModel.getAllSheets().collectLatest {newItems: PagingData<Sheet> ->
                sheetPagingAdapter.submitData(newItems)
            }
        }
        coroutineHandler.foregroundScope.launch {
            viewModel.sheetItemCount.collectLatest {count ->
                dataBinding.layoutEmptyViewSheets.root.visibility = if (count == 0L){
                    View.VISIBLE
                }else{
                    View.GONE
                }
            }
        }
        dataBinding.rvSheetList.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerSpacingItemDecoration(8))
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