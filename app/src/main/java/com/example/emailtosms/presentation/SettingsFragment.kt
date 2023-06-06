package com.example.emailtosms.presentation

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.emailtosms.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {
        fun getInstance() = SettingsFragment()
    }

}
