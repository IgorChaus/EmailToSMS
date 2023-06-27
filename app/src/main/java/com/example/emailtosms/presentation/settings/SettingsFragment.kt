package com.example.emailtosms.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.emailtosms.R


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)

        setClearLogListener()
    }

    fun setClearLogListener(){
        val clearLogButton = findPreference<Preference>("clear_log")
        clearLogButton?.setOnPreferenceClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление Лога")
            builder.setMessage(
                "Вы собираетесь удалить Лог приложения. Его удаление повлечет за собой " +
                        "удаление всей истории отправленных сообщений.\n\nВы уверены?"
            )
            builder.setNegativeButton(
                "Нет"
            ) { dialog, which -> dialog.cancel() }

            builder.setPositiveButton("Да") { dialog, which ->
                viewModel.deleteAllSmsItems()
            }
            builder.show()
            true
        }
    }


    companion object {
        fun getInstance() = SettingsFragment()
    }

}
