package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.viewholder.PreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class MainSettingsFragment: PreferenceFragmentCompat() {

    val viewModel: PreferenceViewModel by viewModels()
    val coroutineHandler = CoroutineHandler(Job())

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)

    }
}