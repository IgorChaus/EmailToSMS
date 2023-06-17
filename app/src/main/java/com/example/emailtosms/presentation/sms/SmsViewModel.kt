package com.example.emailtosms.presentation.sms

import androidx.lifecycle.ViewModel
import com.example.emailtosms.data.sms.SmsListRepositoryTest
import com.example.emailtosms.domain.sms.GetSmsListUseCase


class SmsViewModel: ViewModel() {
    private val repository = SmsListRepositoryTest()

    private val getSmsListUseCase = GetSmsListUseCase(repository)

    val smsList = getSmsListUseCase.getSmsList()
}