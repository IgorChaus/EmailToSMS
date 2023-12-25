package com.example.emailtosms.domain.email

interface EmailListRepository {

    suspend fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String,
        startMessageNumber: Int
    ): EmailResponse

    suspend fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        token: String,
        isDeleted: Boolean
    ): EmailResponse

}