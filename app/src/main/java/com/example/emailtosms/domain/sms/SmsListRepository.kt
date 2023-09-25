package com.example.emailtosms.domain.sms

interface SmsListRepository {

    suspend fun getSmsList(limit: Int): List<SmsItem>

    suspend fun addSmsItem(smsItem: SmsItem)

    suspend fun deleteAllSmsItems()
}