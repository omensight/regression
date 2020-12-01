package com.alphemsoft.education.regression.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.constants.IntentResultConstants.Companion.RESULT_IMPORT_DATA
import com.alphemsoft.education.regression.constants.IntentResultConstants.Companion.SUPPORTED_IMPORT_MIME_TYPES
import com.alphemsoft.education.regression.constants.PREMIUM_REQUEST_IMPORT_DATA
import com.alphemsoft.education.regression.constants.PermissionConstants
import com.alphemsoft.education.regression.databinding.FragmentDataSheetBinding
import com.alphemsoft.education.regression.dataparser.CsvParser
import com.alphemsoft.education.regression.extensions.displayMetrics
import com.alphemsoft.education.regression.ui.SimpleFieldModelUi
import com.alphemsoft.education.regression.ui.adapter.DataPointAdapter
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.util.PermissionHandler
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileReader
import java.util.*
import javax.inject.Inject

abstract class BaseDataSheetFragment : BaseFragment<FragmentDataSheetBinding, DataSheetViewModel>(
    layoutId = R.layout.fragment_data_sheet,
    viewModelId = BR.data_sheet_viewmodel,
    R.menu.menu_data_sheet_detail
), PremiumFeatureDialogFragment.OnPremiumDecisionListener

@AndroidEntryPoint
class DataSheetFragment : BaseDataSheetFragment() {

    override val viewModel: DataSheetViewModel by activityViewModels()
    val args: DataSheetFragmentArgs by navArgs()
    private var actionMode: ActionMode? = null
    private lateinit var actionCallback: ActionMode.Callback

    @Inject
    lateinit var dataPointAdapter: DataPointAdapter
//    private lateinit var importEntriesDialogFragment: ImportEntriesDialogFragment

    lateinit var singleFieldDataSheetFragment: SingleFieldDataSheetFragmentSimple

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
        setupBottomAppBar()
        setupContextMenu()
        setupPermissionHandler()
    }

    private fun setupPermissionHandler() {
    }

    private fun setupSingleFieldBottomSheet() {
        singleFieldDataSheetFragment = SingleFieldDataSheetFragmentSimple()
        val modelUi = SimpleFieldModelUi(
            getString(R.string.action_add_empty_data_points),
            getString(R.string.hint_number_of_data_points),
            getString(R.string.description_add_data_points)
        )
        singleFieldDataSheetFragment.setModelUi(modelUi)
        singleFieldDataSheetFragment.setSimpleFieldListener(object : SimpleFieldListener<Int> {
            override fun onFieldFilled(field: Int) {
                coroutineHandler.backgroundScope.launch {
                    viewModel.addTemporaryPoints(field, args.sheetId)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_validate_and_save -> {
                if (viewModel.dataEntries.value?.size ?: 0 < 3) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.error_not_enough_entries),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    coroutineHandler.backgroundScope.launch {
                        val saved: Boolean = viewModel.validateAndSaveData()
                        if (saved) {
                            viewModel.getDataPointList(args.sheetId)
                            Snackbar.make(
                                requireView(),
                                getString(R.string.saved),
                                Snackbar.LENGTH_LONG
                            ).show()
                            coroutineHandler.foregroundScope.launch {
                                val action = DataSheetFragmentDirections.actionResults(args.sheetId)
                                findNavController().navigate(action)
                            }
                        } else {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.error_invalid_data_points),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                true
            }
            R.id.action_import_csv_data -> {
//                val fragment = PremiumFeatureDialogFragment()
//                fragment.show(childFragmentManager, this, PREMIUM_REQUEST_IMPORT_DATA, R.string.action_import_csv_data)
                onRewardedVideoWatched(PREMIUM_REQUEST_IMPORT_DATA)
                true
            }
            R.id.action_export -> {
                startDataExport()
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
                        coroutineHandler.foregroundScope.launch {
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
                    actionMode?.let {
                        coroutineHandler.backgroundScope.launch {
                            viewModel.selectNothing()
                        }
                    } ?: run {
                        actionMode = requireActivity().startActionMode(actionCallback)
                    }
                }

                R.id.action_select_all -> {
                    coroutineHandler.foregroundScope.launch {
                        dataPointAdapter.selectable = true
                        viewModel.selectAll()
                        actionMode ?: run {
                            actionMode = requireActivity().startActionMode(actionCallback)
                        }
                    }
                }

                R.id.action_add_bunch -> {
                    setupSingleFieldBottomSheet()
                    singleFieldDataSheetFragment.show(
                        requireActivity().supportFragmentManager,
                        "SingleFieldBottomSheet"
                    )
                }
            }
            true
        }
    }

    private fun setUpAdapter() {
        coroutineHandler.foregroundScope.launch {
            val sheet = viewModel.getSheet(args.sheetId)
            sheet?.let { safeSheet ->
                dataBinding.rvDataPoints.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//                    addItemDecoration(
//                        DividerItemDecoration(
//                            resources.getDimensionPixelSize(R.dimen.small_spacing)
//                        )
//                    )
                    val metrics = requireActivity().displayMetrics()
                    dataPointAdapter.metrics = metrics
                    adapter = dataPointAdapter
                }

                coroutineHandler.foregroundScope.launch {
                    viewModel.getDataPointList(args.sheetId).collectLatest { it ->
                        dataPointAdapter.addNewItems(it.filter { item -> !item.deleted })
                    }
                }
            }
        }
    }

    override fun onCancelSelected(requestId: Int) {

    }

    override fun onGetASubscriptionSelected(requestId: Int) {

    }

    override fun onRewardedVideoWatched(requestId: Int) {
        when (requestId) {
            PREMIUM_REQUEST_IMPORT_DATA -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                val mimeTypeMap = MimeTypeMap.getSingleton()
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_MIME_TYPES, SUPPORTED_IMPORT_MIME_TYPES)
                startActivityForResult(intent, RESULT_IMPORT_DATA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        uri?.let { safeUri ->
            val mime = requireActivity().contentResolver.getType(uri) ?: ""
            val inputStream = requireActivity().contentResolver.openInputStream(safeUri)
            val cachePath = requireActivity().externalCacheDir?.absoluteFile
            val file = File(cachePath, UUID.randomUUID().toString())
            file.outputStream().use {
                inputStream?.copyTo(it)
            }
            val types =
                arrayOf("application/csv", "text/comma-separated-values", "text/csv", "text/plain")
            if (types.contains(mime)) {
                val csvReader = CSVParser.parse(FileReader(file), CSVFormat.DEFAULT)
                val csvHelper = CsvParser(csvReader)

                val entries = csvHelper.sheetEntries
                coroutineHandler.foregroundScope.launch {
                    viewModel.addImportedEntries(entries, args.sheetId)
                    val dest =
                        DataSheetFragmentDirections.actionDestinationDataSheetToDestinationImportDataFromDataSheet(
                            csvHelper.errorCount
                        )
                    findNavController().navigate(dest)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsAccepted =
            PermissionHandler.checkAcceptedPermissions(permissions, grantResults)
        if (permissionsAccepted) {
            when (requestCode) {
                PermissionConstants.STORAGE_PERMISSION_REQUEST_CODE -> {
                    startDataExport()
                }
            }
        }
    }

    private fun startDataExport() {
        val hasWritePermission = PermissionHandler.checkSinglePermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (hasWritePermission) {
            val exportDataDestination = DataSheetFragmentDirections.actionExportData()
            findNavController().navigate(exportDataDestination)
        } else {
            var dialog: AlertDialog? = null
            val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                .setTitle(R.string.storage_permission_request_title)
                .setMessage(R.string.storage_permission_request_message)
                .setNegativeButton(R.string.no_thanks) { _, _ ->
                    dialog?.dismiss()
                }
                .setPositiveButton(R.string.grant) { _, _ ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PermissionConstants.STORAGE_PERMISSION_REQUEST_CODE
                    )
                }
            dialog = dialogBuilder.create()
            dialog.show()
        }
    }
}
