package com.alphemsoft.education.regression.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.alphemsoft.education.regression.R
import com.alphemsoft.education.regression.coroutines.CoroutineHandler
import com.alphemsoft.education.regression.ui.viewholder.PreferenceViewModel
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class MainSettingsFragment : PreferenceFragmentCompat() {

    private lateinit var manager: ReviewManager
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
        manager = ReviewManagerFactory.create(requireContext())

        findPreference<Preference>(getString(R.string.key_preference_buy_subscription))?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.destination_purchase_subscription)
            true
        }

        findPreference<Preference>(getString(R.string.key_preference_rate_us))?.run {
            this.setOnPreferenceClickListener { _->
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener {
                    val isSuccessful = it.isSuccessful
                    if (it.isSuccessful){
                        val flow = manager.launchReviewFlow(requireActivity(), it.result)
                        flow.addOnCompleteListener {
                            Log.d("Review", "Completed")
                        }
                    }
                }
                true
            }
        }

        findPreference<Preference>(getString(R.string.key_preference_about_us))?.run {
            this.setOnPreferenceClickListener { ass->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=com.alphemsoft.education.regression")
                    setPackage("com.android.vending")
                }
                context.startActivity(intent)
                true
            }
        }
    }
}