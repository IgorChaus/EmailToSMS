package com.example.emailtosms.presentation.sms

import android.app.Application
import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
import com.example.emailtosms.data.mapper.MapperEmailToSms
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.EmailResponse
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import com.example.emailtosms.domain.sms.GetSmsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class SmsViewModel @Inject constructor(
    private val application: Application,
    private val getSmsListUseCase: GetSmsListUseCase,
    private val addSmsItemUseCase: AddSmsItemUseCase,
    private val mapperEmailToSms: MapperEmailToSms,
    private val getEmailListWithTokenUseCase: GetEmailListWithTokenUseCase
) : ViewModel() {

    private val sharePref = PreferenceManager.getDefaultSharedPreferences(application)
    private val limit = sharePref.getString("len_log", "20")?.toInt() ?: 20
    val smsList = getSmsListUseCase(limit)

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private lateinit var emailResponse: EmailResponse

    fun checkEmail(permission: Boolean) {

        val sharePref = PreferenceManager.getDefaultSharedPreferences(application)
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