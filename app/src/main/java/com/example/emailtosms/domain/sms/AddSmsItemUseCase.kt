package com.example.emailtosms.domain.sms

class AddSmsItemUseCase(private val smsListRepository: SmsListRepository) {
    suspend fun addSmsItem(smsItem: SmsItem){
        smsListRepository.addSmsItem(smsItem)
    }
}