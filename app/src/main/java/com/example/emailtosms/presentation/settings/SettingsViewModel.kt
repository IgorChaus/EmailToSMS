package com.example.emailtosms.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailtosms.domain.sms.DeleteAllSmsItemsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val deleteAllSmsItemsUseCase: DeleteAllSmsItemsUseCase
) : ViewModel() {

    fun deleteAllSmsItems(){
        viewModelScope.launch{
            deleteAllSmsItemsUseCase()
        }
    }
}