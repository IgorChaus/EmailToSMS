package com.example.emailtosms.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.example.emailtosms.data.workers.RefreshEmailWorker

class BootCompleteReceiver : BroadcastReceiver() {
    var context: Context? = null

    override fun onReceive(_context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context = _context
            val sharePref = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
            val checkInterval =
                sharePref?.getString("check_interval", "Не проверять") ?: "Не проверять"
            when (checkInterval) {
                "Не проверять" -> cancelWorker()
                "Каждые 15 минут" -> startWorker(15)
                "Каждые 30 минут" -> startWorker(30)
                "Каждый час" -> startWorker(60)
            }
        }
    }

    private fun startWorker(intervalInMinutes: Long){
        context?.let{
            val workManager = WorkManager.getInstance(it)
            workManager.enqueueUniquePeriodicWork(
                RefreshEmailWorker.NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                RefreshEmailWorker.makeRequest(intervalInMinutes)
            )
        }
    }

    private fun cancelWorker() {
        context?.let{
            val workManager = WorkManager.getInstance(it)
            workManager.cancelUniqueWork(RefreshEmailWorker.NAME)
        }
    }
}