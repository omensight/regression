package com.alphemsoft.education.regression.ui.fragment

import androidx.fragment.app.viewModels
import com.alphemsoft.education.regression.BR
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.databinding.DataPointListFragmentBinding
import com.alphemsoft.education.regression.ui.base.BaseFragment
import com.alphemsoft.education.regression.viewmodel.DataSheetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataPointListFragment : BaseFragment<DataPointListFragmentBinding, DataSheetViewModel>(
    R.layout.data_point_list_fragment,
    BR.sheet_list_view_model,
    null
) {
    override val viewModel: DataSheetViewModel by viewModels()
}
