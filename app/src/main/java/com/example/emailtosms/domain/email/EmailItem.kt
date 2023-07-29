package com.example.emailtosms.domain.email

import java.util.Date

data class EmailItem(
    val date: String,
    val address: String,
    val subject: String,
    val message: String
)