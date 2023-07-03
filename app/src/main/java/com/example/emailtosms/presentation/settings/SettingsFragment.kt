package com.example.emailtosms.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.emailtosms.R
import com.example.emailtosms.data.workers.RefreshEmailWorker


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)

        setClearLogListener()

        val checkEmailInterval = findPreference<Preference>("check_interval")
        checkEmailInterval?.setOnPreferenceChangeListener { preference, newValue ->
            when(newValue){
                "Не проверять"      -> cancelWorker()
                "Каждые 15 минут"   -> startWorker(15)
                "Каждые 30 минут"   -> startWorker(30)
                "Каждый час"        -> startWorker(60)
            }
            true
        }

    }

    private fun setClearLogListener(){
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

    private fun startWorker(intervalInMinutes: Long){
        val application = requireActivity().application
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniquePeriodicWork(
            RefreshEmailWorker.NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            RefreshEmailWorker.makeRequest(intervalInMinutes)
        )
    }

    private fun cancelWorker() {
        val application = requireActivity().application
        val workManager = WorkManager.getInstance(application)
        workManager.cancelUniqueWork(RefreshEmailWorker.NAME)
    }

    companion object {
        fun getInstance() = SettingsFragment()
    }

}
