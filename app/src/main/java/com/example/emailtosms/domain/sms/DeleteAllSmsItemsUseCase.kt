package com.example.emailtosms.domain.sms

class DeleteAllSmsItemsUseCase(private val smsListRepository: SmsListRepository) {
    fun deleteAllSmsItems(){
        smsListRepository.deleteAllSmsItems()
    }
}