package com.example.emailtosms.domain.sms

import androidx.lifecycle.LiveData

class GetSmsListUseCase(private val smsListRepository: SmsListRepository) {
    operator fun invoke(limit: Int): LiveData<List<SmsItem>> = smsListRepository.getSmsList(limit)
}