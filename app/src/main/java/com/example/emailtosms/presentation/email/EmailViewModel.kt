package com.example.emailtosms.presentation.email

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
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

    private val _emailResponse = MutableLiveData<EmailResponse>()
    val emailResponse: LiveData<EmailResponse>
        get() = _emailResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getEmailSettings()
            _loading.postValue(true)
            val emailList = getEmailListUseCase(user, password, host, port, 1)
            _emailResponse.postValue(emailList)
            _loading.postValue(false)
        }
    }

    fun checkEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            getEmailSettings()
            val emailList = getEmailListUseCase(user, password, host, port, 1)
            _emailResponse.postValue(emailList)
            _loading.postValue(false)
        }
    }

    fun getEmailSettings(){
        val sharePref = PreferenceManager.getDefaultSharedPreferences(application)
        user = sharePref.getString("email", BuildConfig.EMAIL) ?: ""
        password = sharePref.getString("password", BuildConfig.PASSWORD) ?: ""
        host = sharePref.getString("server","imap.mail.ru") ?: ""
        port = sharePref.getString("port","995") ?: ""
    }
}