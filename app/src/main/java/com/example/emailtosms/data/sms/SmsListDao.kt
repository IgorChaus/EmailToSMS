package com.example.emailtosms.data.sms

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.emailtosms.data.sms.SmsItemDbModel

@Dao
interface SmsListDao {

    @Query("SELECT * FROM sms_items")
    fun getSmsList(): LiveData<List<SmsItemDbModel>>

    @Insert()
    suspend fun addSmsItem(smsItemDbModel: SmsItemDbModel)

    @Query("DELETE FROM sms_items")
    suspend fun deleteAllSmsItems()
}
