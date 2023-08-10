package com.example.emailtosms.domain.sms

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetSmsListUseCase @Inject constructor(private val smsListRepository: SmsListRepository) {
    operator fun invoke(limit: Int): LiveData<List<SmsItem>> = smsListRepository.getSmsList(limit)
}