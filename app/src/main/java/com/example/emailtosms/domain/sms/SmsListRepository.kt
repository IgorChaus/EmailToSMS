package com.example.emailtosms.domain.sms

import androidx.lifecycle.LiveData

interface SmsListRepository {

    fun getSmsList(limit: Int): LiveData<List<SmsItem>>

    suspend fun addSmsItem(smsItem: SmsItem)

    suspend fun deleteAllSmsItems()
}