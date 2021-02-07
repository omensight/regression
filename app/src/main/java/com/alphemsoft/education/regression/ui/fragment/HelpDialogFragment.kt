package com.alphemsoft.education.regression.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.secondary.Help
import com.alphemsoft.education.regression.databinding.DialogFragmentHelpBinding
import com.alphemsoft.education.regression.ui.adapter.HelpAdapter
import com.alphemsoft.education.regression.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HelpDialogFragment : BaseDialogFragment<DialogFragmentHelpBinding>(
    R.layout.dialog_fragment_help
) {
    private val helpAdapter = HelpAdapter()

    override fun onResume() {
        super.onResume()
        setupHelpList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val lp = WindowManager.LayoutParams()
        val window: Window? = dialog.window

        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        return dialog
    }

    private fun setupHelpList() {
        helpAdapter.addNewItems(
            listOf(
                Help(
                    R.drawable.ic_import,
                    R.string.import_data,
                    R.string.help_description_import_data
                ),
                Help(R.drawable.ic_export, R.string.title_dialog_export_data, R.string.help_description_export_data),
                Help(R.drawable.ic_content_save, R.string.action_validate_and_save),
                Help(R.drawable.ic_add_box, R.string.add_new_data_point),
                Help(R.drawable.ic_add_multiple_box, R.string.action_add_empty_data_points),
                Help(R.drawable.ic_select, R.string.select),
                Help(R.drawable.ic_select_all, R.string.select_all),
                Help(R.drawable.ic_sweep_outline, R.string.action_sweep_all),
            )
        )
        dataBinding.rvHelp.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = helpAdapter
        }
    }
}