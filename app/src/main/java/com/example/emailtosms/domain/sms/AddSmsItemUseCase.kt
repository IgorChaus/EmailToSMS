package com.example.emailtosms.domain.sms

import javax.inject.Inject

class AddSmsItemUseCase @Inject constructor(private val smsListRepository: SmsListRepository) {
    suspend operator fun invoke(smsItem: SmsItem){
        smsListRepository.addSmsItem(smsItem)
    }
}