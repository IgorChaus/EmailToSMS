package com.example.emailtosms.domain.sms

import javax.inject.Inject

class GetSmsListUseCase @Inject constructor(private val smsListRepository: SmsListRepository) {
    suspend operator fun invoke(limit: Int): List<SmsItem> = smsListRepository.getSmsList(limit)
}