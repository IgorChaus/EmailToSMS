package com.example.emailtosms.domain

data class SmsItem(
    var id: Int = UNDEFIND_ID,
    val date: String,
    val phone: String,
    val message: String

    ){

    companion object {
        const val UNDEFIND_ID = 0
    }
}