package com.example.emailtosms.domain

class DeleteAllSmsItems(private val smsListRepository: SmsListRepository) {
    fun deleteAllSmsItems(){
        smsListRepository.deleteAllSmsItems()
    }
}