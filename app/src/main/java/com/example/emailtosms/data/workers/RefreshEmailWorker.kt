package com.example.emailtosms.data.workers

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.emailtosms.data.email.EmailListRepositoryImpl
import com.example.emailtosms.data.sms.SmsListRepositoryImpl
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import com.example.emailtosms.domain.sms.MapperEmailToSms
import com.example.emailtosms.domain.sms.SmsItem
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class RefreshEmailWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    private val smsRepository = SmsListRepositoryImpl(context)
    private val emailRepository = EmailListRepositoryImpl()

    private val addSmsItemUseCase = AddSmsItemUseCase(smsRepository)
    private val mapperEmailToSms = MapperEmailToSms()
    private val getEmailListWithTokenUseCase = GetEmailListWithTokenUseCase(emailRepository)

    private val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
    private val user = sharePref.getString("email", "") ?: ""
    private val password = sharePref.getString("password", "") ?: ""
    private val host = sharePref.getString("server", "") ?: ""
    private val port = sharePref.getString("port", "") ?: ""
    private val message_action = sharePref.getString("message_action", "") ?: ""
    private val token = sharePref.getString("token", "") ?: ""

    override suspend fun doWork(): Result {
        val emailResponse =
            getEmailListWithTokenUseCase.getEmailListWithToken(
                user,
                password,
                host,
                port,
                token,
                true
            )

        if (emailResponse.responseCode == EmailListRepositoryImpl.OK) {
            for (item in emailResponse.emailItemList) {
                val dateFormat = SimpleDateFormat("dd.MM", Locale("ru", "RU"))
                val date = item.date?.let { dateFormat.format(it) } ?: ""
                val result = mapperEmailToSms.mapEmailMessageToSmsMessage(item.message)
                val phone = result["phone"] ?: ""
                val message = result["message"] ?: ""
                addSmsItemUseCase.addSmsItem(SmsItem(SmsItem.UNDEFIND_ID, date, phone, message))
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