package com.example.emailtosms.domain.email

import com.example.emailtosms.domain.email.EmailItem

data class EmailResponse(
    val emailItemList: List<EmailItem>,
    val responseCode: String?
)