package com.example.emailtosms.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailtosms.data.database.SmsListRepositoryImpl
import com.example.emailtosms.domain.sms.DeleteAllSmsItemsUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val smsRepository = SmsListRepositoryImpl(application)

    private val deleteAllSmsItemsUseCase = DeleteAllSmsItemsUseCase(smsRepository)

    fun deleteAllSmsItems(){
        viewModelScope.launch{
            deleteAllSmsItemsUseCase.deleteAllSmsItems()
        }
    }
}