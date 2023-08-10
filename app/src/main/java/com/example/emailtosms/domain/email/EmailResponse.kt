package com.example.emailtosms.domain.email

data class EmailResponse(
    val emailItemList: List<EmailItem>,
    val responseCode: String?
)