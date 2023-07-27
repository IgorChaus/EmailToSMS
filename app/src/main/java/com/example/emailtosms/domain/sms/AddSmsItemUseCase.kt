package com.example.emailtosms.domain.sms

class AddSmsItemUseCase(private val smsListRepository: SmsListRepository) {
    suspend operator fun invoke(smsItem: SmsItem){
        smsListRepository.addSmsItem(smsItem)
    }
}