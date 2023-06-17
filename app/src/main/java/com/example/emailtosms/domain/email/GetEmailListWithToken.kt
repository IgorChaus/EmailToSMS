package com.example.emailtosms.domain.email

class GetEmailListWithToken(private val emailListRepository: EmailListRepository) {
    fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        isDeleted: Boolean
    ): EmailResponse =
        emailListRepository.getEmailListWithToken(user, password, host, port, isDeleted)
}