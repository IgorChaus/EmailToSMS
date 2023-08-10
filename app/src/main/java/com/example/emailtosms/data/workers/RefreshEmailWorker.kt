package com.example.emailtosms.data.workers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.emailtosms.BuildConfig
import com.example.emailtosms.data.mapper.MapperEmailToSms
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import java.util.concurrent.TimeUnit

class RefreshEmailWorker(
    val context: Context,
    workerParameters: WorkerParameters,
    private val addSmsItemUseCase: AddSmsItemUseCase,
    private val getEmailListWithTokenUseCase: GetEmailListWithTokenUseCase,
    private val mapperEmailToSms: MapperEmailToSms
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED) {
            val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
            val user = sharePref.getString("email", BuildConfig.EMAIL) ?: ""
            val password = sharePref.getString("password",BuildConfig.PASSWORD) ?: ""
            val host = sharePref.getString("server","imap.mail.ru") ?: ""
            val port = sharePref.getString("port","995") ?: ""
            val message_action = sharePref.getString("message_action", "") ?: ""
            val token = sharePref.getString("token", "1111") ?: ""
            val emailResponse =
                getEmailListWithTokenUseCase(
                    user,
                    password,
                    host,
                    port,
                    token,
                    true
                )

            if (emailResponse.responseCode == EmailListRepositoryImpl.OK) {
                for (item in emailResponse.emailItemList) {
                    val smsItem = mapperEmailToSms.mapEmailItemToSmsItem(item)
                    addSmsItemUseCase(smsItem)
                    SmsManager.getDefault().sendTextMessage(
                        smsItem.phone,
                        null,
                        smsItem.message,
                        null,
                        null
                    )
                }
            }
        }
        return Result.success()
    }

    companion object{
        const val NAME = "RefreshEmailWorker"

        fun makeRequest(intervalInMinutes: Long): PeriodicWorkRequest{
            return PeriodicWorkRequestBuilder<RefreshEmailWorker>(intervalInMinutes, TimeUnit.MINUTES)
                .addTag(NAME)
                .build()
        }
    }

}