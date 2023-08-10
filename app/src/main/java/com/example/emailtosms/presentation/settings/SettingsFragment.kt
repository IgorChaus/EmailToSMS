package com.example.emailtosms.presentation.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.emailtosms.R
import com.example.emailtosms.data.workers.RefreshEmailWorker
import com.example.emailtosms.presentation.EmailToListApp
import com.example.emailtosms.presentation.ViewModelFactory
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SettingsViewModel
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var preferenceInterval: Preference? = null
    private var interval: Any = "Каждые 15 минут"

    private val component by lazy {
        (requireActivity().application as EmailToListApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(SettingsViewModel::class.java)

        setClearLogListener()
        registerPermissionListener()

        preferenceInterval = findPreference<Preference>("check_interval")
        preferenceInterval?.let{
            it.setOnPreferenceChangeListener { _, newValue ->
                handleChoice(newValue)
                true
            }
            checkPermission()
        }
    }

    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            preferenceInterval?.setSummaryProvider {
                "Не проверять"
            }
            cancelWorker()
        }
    }

    private fun handleChoice(choice: Any){
        if (choice == "Не проверять"){
            cancelWorker()
        } else {
            interval = choice
            when {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED -> {
                    startService()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) -> {
                    Toast.makeText(
                        requireContext(),
                        "Для отправки SMS сообщений приложению необходимо разрешение на отправку SMS",
                        Toast.LENGTH_LONG
                    ).show()
                    preferenceInterval?.setSummaryProvider {
                        "Не проверять"
                    }
                    cancelWorker()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                }
            }
        }
    }

    private fun startService(){
        when(interval){
            "Каждые 15 минут"   -> startWorker(15)
            "Каждые 30 минут"   -> startWorker(30)
            "Каждый час"        -> startWorker(60)
            else                -> cancelWorker()
        }
    }

    private fun registerPermissionListener(){
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                startService()
            }else{
                Toast.makeText(
                    requireContext(),
                    "Отправка SMS выполняться не будет",
                    Toast.LENGTH_LONG
                ).show()
                preferenceInterval?.setSummaryProvider {
                    "Не проверять"
                }
                cancelWorker()
            }
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
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
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
