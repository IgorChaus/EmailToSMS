package com.example.emailtosms.domain.sms

import androidx.lifecycle.LiveData
import com.example.emailtosms.domain.sms.SmsItem

interface SmsListRepository {

    fun getSmsList(): LiveData<List<SmsItem>>

    suspend fun addSmsItem(smsItem: SmsItem)

    suspend fun deleteAllSmsItems()
}