package com.example.emailtosms.domain.sms

import androidx.lifecycle.LiveData
import com.example.emailtosms.domain.sms.SmsItem

interface SmsListRepository {

    fun getSmsList(): LiveData<List<SmsItem>>

    fun addSmsItem(smsItem: SmsItem)

    fun deleteAllSmsItems()
}