package com.example.emailtosms.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.emailtosms.R
import com.example.emailtosms.presentation.sms.SmsViewModel


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SmsViewModel::class.java)

        val clearLogButton = findPreference<Preference>("clear_log")
        clearLogButton?.setOnPreferenceClickListener {
            Log.i("MyTag", "click")
            true
        }


    }


    companion object {
        fun getInstance() = SettingsFragment()
    }

}
