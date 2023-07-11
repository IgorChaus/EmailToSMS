package com.example.emailtosms.presentation.sms

import android.app.Application
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
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

    val smsList = getSmsListUseCase.getSmsList()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private lateinit var emailResponse: EmailResponse

    fun checkEmail(permission: Boolean) {

        val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
        val user = sharePref.getString("email", BuildConfig.EMAIL) ?: ""
        val password = sharePref.getString("password", BuildConfig.PASSWORD) ?: ""
        val host = sharePref.getString("server","imap.mail.ru") ?: ""
        val port = sharePref.getString("port","995") ?: ""
        val message_action = sharePref.getString("message_action", "") ?: ""
        val token = sharePref.getString("token", "1111") ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            if (permission) {
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
                        addSmsItemUseCase.addSmsItem(
                            SmsItem(
                                SmsItem.UNDEFIND_ID,
                                date,
                                phone,
                                message
                            )
                        )
                        SmsManager.getDefault().sendTextMessage(
                            phone,
                            null,
                            message,
                            null,
                            null
                        )
                    }
                }
            }
            _loading.postValue(false)
        }
    }

}