package com.example.emailtosms.domain

class AddSmsItemUseCase(private val smsListRepository: SmsListRepository) {
    fun addSmsItem(smsItem: SmsItem){
        smsListRepository.addSmsItem(smsItem)
    }
}