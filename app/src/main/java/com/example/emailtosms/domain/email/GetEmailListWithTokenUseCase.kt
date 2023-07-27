package com.example.emailtosms.domain.email

class GetEmailListWithTokenUseCase(private val emailListRepository: EmailListRepository) {
    suspend operator fun invoke(
        user: String,
        password: String,
        host: String,
        port: String,
        token: String,
        isDeleted: Boolean
    ): EmailResponse =
        emailListRepository.getEmailListWithToken(user, password, host, port, token, isDeleted)
}