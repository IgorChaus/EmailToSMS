package com.example.emailtosms.presentation.email

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailResponse
import com.example.emailtosms.domain.email.GetEmailListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmailViewModel @Inject constructor(
    private val application: Application,
    private val getEmailListUseCase: GetEmailListUseCase
): ViewModel() {

    private lateinit var user: String
    private lateinit var password: String
    private lateinit var host: String
    private lateinit var port: String

    private var loadMessageNumber: Int = 0

    private val emailMessages = arrayListOf<EmailItem>()

    private val _emailResponse = MutableLiveData<EmailResponse>()
    val emailResponse: LiveData<EmailResponse>
        get() = _emailResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        _loading.postValue(true)
       checkEmail()
    }

    fun getLoadMessageNumber() = loadMessageNumber

    fun checkEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            emailMessages.clear()
            loadMessageNumber = 0
            getEmailSettings()
            val emailList = getEmailListUseCase(user, password, host, port, 0)
            emailMessages.addAll(emailList.emailItemList)
            if (emailList.emailItemList.size == EmailListRepositoryImpl.NUMBER_DOWNLOAD_MESSAGES){
                loadMessageNumber = EmailListRepositoryImpl.NUMBER_DOWNLOAD_MESSAGES
            }
            _emailResponse.postValue(emailList)
            _loading.postValue(false)
        }

    }

    fun getNextMessages(){
        if (_loading.value == true || loadMessageNumber == 0) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            getEmailSettings()

            Log.i("MyTag", "startMessageNumber $loadMessageNumber")

            val emailList = getEmailListUseCase(user, password, host, port, loadMessageNumber)
            Log.i("MyTag", "emailList.size ${emailList.emailItemList.size}")

            if (emailList.emailItemList.isNotEmpty()) {
                loadMessageNumber = loadMessageNumber + emailList.emailItemList.size
                emailMessages.addAll(emailList.emailItemList)
                Log.i("MyTag", "emailMessages.size ${emailMessages.size}")
                _emailResponse.postValue(EmailResponse(emailMessages, emailList.responseCode))
            }
            _loading.postValue(false)
        }
    }


    private fun getEmailSettings(){
        val sharePref = PreferenceManager.getDefaultSharedPreferences(application)
        user = sharePref.getString("email", BuildConfig.EMAIL) ?: ""
        password = sharePref.getString("password", BuildConfig.PASSWORD) ?: ""
        host = sharePref.getString("server","imap.mail.ru") ?: ""
        port = sharePref.getString("port","995") ?: ""
    }
}