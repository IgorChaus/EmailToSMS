package com.example.emailtosms.domain.email

import com.example.emailtosms.domain.email.EmailItem

interface EmailListRepository {

    fun getEmailList(): EmailResponse

}