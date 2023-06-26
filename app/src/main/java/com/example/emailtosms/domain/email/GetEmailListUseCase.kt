package com.example.emailtosms.domain.email

import androidx.lifecycle.LiveData

class GetEmailListUseCase(private val emailListRepository: EmailListRepository) {
    fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
    ): LiveData<EmailResponse> = emailListRepository.getEmailList(user, password, host, port)
}
