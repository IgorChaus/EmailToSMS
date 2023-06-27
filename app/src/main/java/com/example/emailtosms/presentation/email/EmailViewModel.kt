package com.example.emailtosms.presentation.email

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.example.emailtosms.data.email.EmailListRepositoryImpl
import com.example.emailtosms.data.email.EmailListRepositoryTest
import com.example.emailtosms.domain.email.GetEmailListUseCase

class EmailViewModel(application: Application): AndroidViewModel(application) {
    private val context = application
    private val repository = EmailListRepositoryTest
    private val getEmailListUseCase = GetEmailListUseCase(repository)
    private val sharePref = PreferenceManager.getDefaultSharedPreferences(context)
    private val user = sharePref.getString("email", "") ?: ""
    private val password = sharePref.getString("password","") ?: ""
    private val host = sharePref.getString("server","") ?: ""
    private val port = sharePref.getString("port","") ?: ""
    private val message_action = sharePref.getString("message_action", "") ?: ""
    private val token = sharePref.getString("token","") ?:""

    var emailResponse = getEmailListUseCase.getEmailList(user, password, host, port)

    fun checkEmail(){
        emailResponse = getEmailListUseCase.getEmailList(user, password, host, port)
    }
}