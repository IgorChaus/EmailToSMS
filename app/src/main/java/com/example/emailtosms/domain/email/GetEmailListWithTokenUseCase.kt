package com.example.emailtosms.domain.email

import javax.inject.Inject

class GetEmailListWithTokenUseCase @Inject constructor(
    private val emailListRepository: EmailListRepository
) {
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