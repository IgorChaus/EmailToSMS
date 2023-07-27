package com.example.emailtosms.domain.sms

class DeleteAllSmsItemsUseCase(private val smsListRepository: SmsListRepository) {
    suspend operator fun invoke(){
        smsListRepository.deleteAllSmsItems()
    }
}