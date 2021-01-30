package com.alphemsoft.education.regression.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.viewholder.PreferenceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class MainSettingsFragment : PreferenceFragmentCompat() {

    val viewModel: PreferenceViewModel by viewModels()
    val coroutineHandler = CoroutineHandler(Job())

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings, rootKey)
        val seekBarPreference = findPreference<SeekBarPreference>(getString(R.string.key_preference_decimal_count))
        seekBarPreference?.apply {
            onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { preference, newValue ->
                    preference as SeekBarPreference
                    preference.summary = getString(R.string.settings_visible_decimals_description).format(newValue)
                    true
                }
            value = seekBarPreference.value
            summary = getString(R.string.settings_visible_decimals_description).format(value)
        }

        findPreference<Preference>(getString(R.string.key_preference_buy_subscription))?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.destination_purchase_subscription)
            true
        }
    }
}