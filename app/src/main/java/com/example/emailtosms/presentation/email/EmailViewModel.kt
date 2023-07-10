package com.example.emailtosms.presentation.email

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.emailtosms.BuildConfig
import com.example.emailtosms.data.email.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.EmailResponse
import com.example.emailtosms.domain.email.GetEmailListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmailViewModel(application: Application): AndroidViewModel(application) {
    private val context = application
    private val repository = EmailListRepositoryImpl()
    private val getEmailListUseCase = GetEmailListUseCase(repository)
    private val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
    private val user = sharePref.getString("email", BuildConfig.EMAIL) ?: ""
    private val password = sharePref.getString("password", BuildConfig.PASSWORD) ?: ""
    private val host = sharePref.getString("server","imap.mail.ru") ?: ""
    private val port = sharePref.getString("port","995") ?: ""

    private val _emailResponse = MutableLiveData<EmailResponse>()
    val emailResponse: LiveData<EmailResponse>
        get() = _emailResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            _emailResponse.postValue(getEmailListUseCase.getEmailList(user, password, host, port))
            _loading.postValue(false)
        }
    }

    fun checkEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            _emailResponse.postValue(getEmailListUseCase.getEmailList(user, password, host, port))
            _loading.postValue(false)
        }
    }
}