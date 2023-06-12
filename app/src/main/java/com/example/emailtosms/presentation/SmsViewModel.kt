package com.example.emailtosms.presentation

import androidx.lifecycle.ViewModel
import com.example.emailtosms.data.sms.SmsListRepositoryTest
import com.example.emailtosms.domain.sms.GetSmsListUseCase


class SmsViewModel: ViewModel() {
    val repository = SmsListRepositoryTest()

    private val getSmsListUseCase = GetSmsListUseCase(repository)

    val smsList = getSmsListUseCase.getSmsList()
}