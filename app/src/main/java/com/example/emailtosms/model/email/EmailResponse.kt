package com.example.emailtosms.model.email

import com.example.emailtosms.domain.email.EmailItem

data class EmailResponse(
    val emailItemList: List<EmailItem>,
    val responseCode: String
)