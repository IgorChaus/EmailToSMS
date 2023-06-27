package com.example.emailtosms.domain.sms

class DeleteAllSmsItemsUseCase(private val smsListRepository: SmsListRepository) {
    suspend fun deleteAllSmsItems(){
        smsListRepository.deleteAllSmsItems()
    }
}