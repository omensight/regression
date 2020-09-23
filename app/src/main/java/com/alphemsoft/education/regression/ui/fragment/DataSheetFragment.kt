package com.alphemsoft.education.regression.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.constants.PREMIUM_REQUEST_IMPORT_DATA
import com.alphemsoft.education.regression.constants.RESULT_IMPORT_DATA
import com.alphemsoft.education.regression.databinding.FragmentDataSheetBinding
import com.alphemsoft.education.regression.extensions.displayMetrics
import com.alphemsoft.education.regression.ui.SimpleFieldModelUi
import com.alphemsoft.education.regression.ui.adapter.QuerySheetColumnAdapter
import com.alphemsoft.education.regression.ui.adapter.itemdecoration.DividerItemDecoration
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.charset.Charset
import javax.inject.Inject

@ExperimentalCoroutinesApi
abstract class BaseDataSheetFragment : BaseFragment<FragmentDataSheetBinding, DataSheetViewModel>(
    layoutId = R.layout.fragment_data_sheet,
    viewModelId = BR.data_sheet_viewmodel,
    menuResourceId = R.menu.menu_data_sheet_detail,
)

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DataSheetFragment : BaseDataSheetFragment(),
    PremiumFeatureDialogFragment.OnPremiumDecisionListener {
    override val viewModel: DataSheetViewModel by viewModels()
    val args: DataSheetFragmentArgs by navArgs()
    private var actionMode: ActionMode? = null
    private lateinit var actionCallback: ActionMode.Callback

    @Inject
    lateinit var dataEntryAdapter: QuerySheetColumnAdapter

    lateinit var singleFieldDataSheetFragment: SingleFieldDataSheetFragment

    @ExperimentalCoroutinesApi
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
            override fun onFieldFilled(field: Int) {
                coroutineHandler.backgroundScope.launch {
                    viewModel.addTemporaryRows(field, args.sheetId)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_validate_and_save->{
                if (viewModel.sheetColumns.value?.size!! <3){
                    Snackbar.make(requireView(), getString(R.string.error_not_enough_entries), Snackbar.LENGTH_LONG).show()
                }else{
                    coroutineHandler.backgroundScope.launch {
                        val saved: Boolean = viewModel.validateAndSaveData()
                        if (saved){
                            viewModel.getSheetDataColumnsPointList(args.sheetId)
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
            R.id.action_import_csv_data->{
                val fragment = PremiumFeatureDialogFragment()
                fragment.show(childFragmentManager, this, PREMIUM_REQUEST_IMPORT_DATA, R.string.action_import_csv_data)
                true
            }
            else -> false
        }
    }

    @ExperimentalCoroutinesApi
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
                            dataEntryAdapter.notifyDataSetChanged()
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
                        viewModel.addTemporaryRows(1,args.sheetId)
                    }
                }

                R.id.action_sweep_all -> {
                    coroutineHandler.backgroundScope.launch {
                        viewModel.sweepAll()
                        requireActivity().runOnUiThread {
                            dataEntryAdapter.notifyDataSetChanged()
                        }
                    }
                }

                R.id.action_select -> {
                    actionMode = requireActivity().startActionMode(actionCallback)
                }

                R.id.action_select_all -> {
                    coroutineHandler.foregroundScope.launch {
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
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//            addItemDecoration(
//                DividerItemDecoration(
//                    16
//                )
//            )
            adapter = dataEntryAdapter
        }

        coroutineHandler.foregroundScope.launch {
            viewModel.getSheetDataColumnsPointList(args.sheetId).collectLatest {
                val displayMetrics = requireActivity().displayMetrics()
                dataEntryAdapter.addNewItems(it, displayMetrics)
            }
        }
    }

    override fun onCancelSelected(requestId: Int) {

    }

    override fun onGetASubscriptionSelected(requestId: Int) {

    }

    override fun onRewardedVideoWatched(requestId: Int) {
        when(requestId){
            PREMIUM_REQUEST_IMPORT_DATA ->{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "text/csv"
                startActivityForResult(intent, RESULT_IMPORT_DATA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        uri?.let { safeUri->
            val inputStream = requireActivity().contentResolver.openInputStream(safeUri)
            val csvParser = CSVParser.parse(inputStream, Charset.defaultCharset(), CSVFormat.EXCEL)
            csvParser.iterator().forEach {

            }
        }
    }

}
