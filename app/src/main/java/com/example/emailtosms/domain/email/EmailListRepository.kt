package com.example.emailtosms.domain.email

import androidx.lifecycle.LiveData

interface EmailListRepository {

    fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
    ): LiveData<EmailResponse>

    fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        isDeleted: Boolean
    ): EmailResponse

}