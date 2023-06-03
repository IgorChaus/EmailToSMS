package com.example.emailtosms.domain

interface EmailListRepository {

    fun getEmailList(): List<EmailItem>

}