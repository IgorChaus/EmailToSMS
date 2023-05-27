package com.example.emailtosms.domain

import androidx.lifecycle.LiveData

class GetSmsListUseCase(private val smsListRepository: SmsListRepository) {
    fun getSmsList(): LiveData<List<SmsItem>> = smsListRepository.getSmsList()
}