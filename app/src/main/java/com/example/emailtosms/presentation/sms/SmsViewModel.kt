package com.example.emailtosms.presentation.sms

import android.app.Application
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
import com.example.emailtosms.data.database.SmsListRepositoryImpl
import com.example.emailtosms.data.mapper.MapperEmailToSms
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.EmailResponse
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import com.example.emailtosms.domain.sms.GetSmsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SmsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private val smsRepository = SmsListRepositoryImpl(application)
    private val emailRepository = EmailListRepositoryImpl()

    private val getSmsListUseCase = GetSmsListUseCase(smsRepository)
    private val addSmsItemUseCase = AddSmsItemUseCase(smsRepository)
    private val mapperEmailToSms = MapperEmailToSms()
    private val getEmailListWithTokenUseCase = GetEmailListWithTokenUseCase(emailRepository)

    private val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
    private val limit = sharePref.getString("len_log", "20")?.toInt() ?: 20
    val smsList = getSmsListUseCase(limit)

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
            _loading.postValue(false)
        }
    }

}