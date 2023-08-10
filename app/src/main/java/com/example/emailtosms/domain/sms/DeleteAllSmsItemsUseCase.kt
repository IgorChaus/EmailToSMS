package com.example.emailtosms.domain.sms

import javax.inject.Inject

class DeleteAllSmsItemsUseCase @Inject constructor(
    private val smsListRepository: SmsListRepository
) {
    suspend operator fun invoke(){
        smsListRepository.deleteAllSmsItems()
    }
}