package com.example.emailtosms.domain

import androidx.lifecycle.LiveData

interface SmsListRepository {

    fun getSmsList(): LiveData<List<SmsItem>>

    fun addSmsItem(smsItem: SmsItem)

    fun deleteAllSmsItems()
}