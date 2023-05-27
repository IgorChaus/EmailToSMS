package com.example.emailtosms.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SmsListDao {

    @Query("SELECT * FROM sms_items")
    fun getSmsList(): LiveData<List<SmsItemDbModel>>

    @Insert()
    fun addSmsItem(smsItemDbModel: SmsItemDbModel)

    @Query("DELETE FROM sms_items")
    fun deleteAllSmsItems()
}
