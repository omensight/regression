package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.FragmentDataSheetBinding
import com.alphemsoft.education.regression.ui.SimpleFieldModelUi
import com.alphemsoft.education.regression.ui.adapter.DataPointAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseDataSheetFragment : BaseFragment<FragmentDataSheetBinding, DataSheetViewModel>(
    layoutId = R.layout.fragment_data_sheet,
    viewModelId = BR.data_sheet_viewmodel,
    menuResourceId = R.menu.menu_data_sheet_detail,
    true
)

@AndroidEntryPoint
class DataSheetFragment : BaseDataSheetFragment() {
    override val viewModel: DataSheetViewModel by viewModels()
    val args: DataSheetFragmentArgs by navArgs()
    private var actionMode: ActionMode? = null
    private lateinit var actionCallback: ActionMode.Callback

    @Inject
    lateinit var dataPointAdapter: DataPointAdapter

    lateinit var singleFieldDataSheetFragment: SingleFieldDataSheetFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        setupBottomAppBar()
        setupContextMenu()
    }

    private fun setupSingleFieldBottomSheet() {
        singleFieldDataSheetFragment = SingleFieldDataSheetFragment()
        val modelUi = SimpleFieldModelUi(
            getString(R.string.action_add_empty_data_points),
            getString(R.string.hint_number_of_data_points),
            getString(R.string.description_add_data_points)
        )
        singleFieldDataSheetFragment.setModelUi(modelUi)
        singleFieldDataSheetFragment.setSimpleFieldListener(object : SimpleFieldListener<Int>{
            override fun onFieldFilled(numberOfEmptyDataPoints: Int) {
                coroutineHandler.backgroundScope.launch {
                    viewModel.addTemporaryPoints(numberOfEmptyDataPoints, args.sheetId)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_validate_and_save->{
                if (viewModel.dataPoints.value.size <3){
                    Snackbar.make(requireView(), getString(R.string.error_not_enough_entries), Snackbar.LENGTH_LONG).show()
                }else{
                    coroutineHandler.backgroundScope.launch {
                        val saved: Boolean = viewModel.validateAndSaveData()
                        if (saved){
                            viewModel.getDataPointList(args.sheetId)
                            Snackbar.make(requireView(), getString(R.string.saved), Snackbar.LENGTH_LONG).show()
                            val action = DataSheetFragmentDirections.actionResults(args.sheetId)
                            findNavController().navigate(action)
                        }else{
                            Snackbar.make(requireView(), getString(R.string.error_invalid_data_points), Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun setupContextMenu() {
        actionCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
                actionMode?.menuInflater?.inflate(R.menu.menu_action_data_sheet, menu)
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, menuItem: MenuItem?): Boolean {
                return when (menuItem?.itemId) {
                    R.id.action_delete_selected -> {
                        coroutineHandler.foregroundScope.launch{
                            viewModel.deleteSelected()
                            delay(500)
                            dataPointAdapter.notifyDataSetChanged()
                            actionMode?.finish()
                            actionMode = null
                        }
                        true
                    }
                    R.id.action_copy_to_another -> {
                        showCopyDataPointsDialog()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                actionMode = null
                dataPointAdapter.selectable = false
                coroutineHandler.foregroundScope.launch {
                    viewModel.selectNothing()
                }
            }

        }
    }

    private fun showCopyDataPointsDialog() {

    }

    @ExperimentalCoroutinesApi

    private fun setupBottomAppBar() {
        dataBinding.bottomAppBarController.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add_data_point -> {
                    coroutineHandler.backgroundScope.launch {
                        viewModel.addTemporaryPoint(args.sheetId)
                    }
                }

                R.id.action_sweep_all -> {
                    coroutineHandler.backgroundScope.launch {
                        viewModel.sweepAll()
                        requireActivity().runOnUiThread {
                            dataPointAdapter.notifyDataSetChanged()
                        }
                    }
                }

                R.id.action_select -> {
                    dataPointAdapter.selectable = true
                    actionMode = requireActivity().startActionMode(actionCallback)
                }

                R.id.action_select_all -> {
                    coroutineHandler.foregroundScope.launch {
                        dataPointAdapter.selectable = true
                        viewModel.selectAll()
                        actionMode = requireActivity().startActionMode(actionCallback)
                    }
                }

                R.id.action_add_bunch ->{
                    setupSingleFieldBottomSheet()
                    singleFieldDataSheetFragment.show(requireActivity().supportFragmentManager, "SingleFieldBottomSheet")
                }
            }
            true
        }
    }

    @ExperimentalCoroutinesApi
    private fun setUpAdapter() {
        dataBinding.rvDataPoints.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    16
                )
            )
            adapter = dataPointAdapter
        }

        coroutineHandler.foregroundScope.launch {
            viewModel.getDataPointList(args.sheetId).collectLatest {
                dataPointAdapter.addNewItems(it)
            }
        }
    }

}
