package com.example.emailtosms.domain.email

class GetEmailListUseCase(private val emailListRepository: EmailListRepository) {
    suspend fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
    ): EmailResponse = emailListRepository.getEmailList(user, password, host, port)
}
