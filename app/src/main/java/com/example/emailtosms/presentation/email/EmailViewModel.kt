package com.example.emailtosms.presentation.email

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
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
    private val user = sharePref.getString("email", "") ?: ""
    private val password = sharePref.getString("password","") ?: ""
    private val host = sharePref.getString("server","") ?: ""
    private val port = sharePref.getString("port","") ?: ""

    private val _emailResponse = MutableLiveData<EmailResponse>()
    val emailResponse: LiveData<EmailResponse>
        get() = _emailResponse

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _emailResponse.postValue(getEmailListUseCase.getEmailList(user, password, host, port))
        }
    }

    fun checkEmail(){
        viewModelScope.launch(Dispatchers.IO) {
            _emailResponse.postValue(getEmailListUseCase.getEmailList(user, password, host, port))
        }
    }
}