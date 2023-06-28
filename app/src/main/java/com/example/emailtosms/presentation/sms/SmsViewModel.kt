package com.example.emailtosms.presentation.sms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.data.email.EmailListRepositoryImpl
import com.example.emailtosms.data.sms.SmsListRepositoryImpl
import com.example.emailtosms.domain.email.EmailResponse
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import com.example.emailtosms.domain.sms.GetSmsListUseCase
import com.example.emailtosms.domain.sms.MapperEmailToSms
import com.example.emailtosms.domain.sms.SmsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SmsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private val smsRepository = SmsListRepositoryImpl(application)
    private val emailRepository = EmailListRepositoryImpl()

    private val getSmsListUseCase = GetSmsListUseCase(smsRepository)
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

    val smsList = getSmsListUseCase.getSmsList()

    private lateinit var emailResponse: EmailResponse

    fun checkEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            emailResponse =
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
        }
    }

}