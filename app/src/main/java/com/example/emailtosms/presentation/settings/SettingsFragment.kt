package com.example.emailtosms.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.emailtosms.R
import com.example.emailtosms.presentation.sms.SmsViewModel


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)

        val clearLogButton = findPreference<Preference>("clear_log")
        clearLogButton?.setOnPreferenceClickListener {
            viewModel.deleteAllSmsItems()
            true
        }


    }


    companion object {
        fun getInstance() = SettingsFragment()
    }

}
