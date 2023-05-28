package com.example.emailtosms.domain

class DeleteAllSmsItemsUseCase(private val smsListRepository: SmsListRepository) {
    fun deleteAllSmsItems(){
        smsListRepository.deleteAllSmsItems()
    }
}