package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.data.model.SheetType
import com.alphemsoft.education.regression.databinding.FragmentCreateSheetBinding
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.CreateSheetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

abstract class AbstractCreateSheetFragment :
    BaseFragment<FragmentCreateSheetBinding, CreateSheetViewModel>(
        layoutId = R.layout.fragment_create_sheet,
        viewModelId = BR.createSheetViewModel,
        menuResourceId = R.menu.menu_create_sheet
    )

@AndroidEntryPoint
class CreateSheetFragment : AbstractCreateSheetFragment() {
    override val viewModel: CreateSheetViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupTypeDropDownMenu()
    }

    private fun setupTypeDropDownMenu() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_simple_dropdown,
            requireContext().resources.getStringArray(R.array.regression_types)
        )
        dataBinding.tilType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                viewModel.newSheet.value = viewModel.newSheet.value?.copy(type = SheetType.values()[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        dataBinding.tilType.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create_sheet_next -> {
                coroutineHandler.backgroundScope.launch {
                    validateAndInsert()
                }
                true
            }
            else -> false
        }
    }

    private suspend fun validateAndInsert(){
        val sheet = viewModel.newSheet.value
        require(sheet != null)
        if (sheet.name.isNotEmpty()){
            val id = viewModel.insertNewSheet()
            coroutineHandler.foregroundScope.launch {
                val action = CreateSheetFragmentDirections.actionDataSheetDetailFromCreateSheet(id)
                findNavController().navigate(action)
            }
        }else{
            requireActivity().runOnUiThread{
                dataBinding.tilLabel.error = getString(R.string.text_error_empty_field)
            }
        }
    }
}