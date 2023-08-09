package com.example.emailtosms.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.emailtosms.data.mapper.MapperSmsItemToEntity
import com.example.emailtosms.domain.sms.SmsItem
import com.example.emailtosms.domain.sms.SmsListRepository

class SmsListRepositoryImpl(context: Context): SmsListRepository {
    private val smsListDao = AppDataBase.getInstance(context).smsListDao()
    private val mapper = MapperSmsItemToEntity()

    override fun getSmsList(limit: Int): LiveData<List<SmsItem>> =
        smsListDao.getSmsList(limit).map {
            mapper.mapListDbModelToListEntity(it)
        }

    override suspend fun addSmsItem(smsItem: SmsItem) {
        smsListDao.addSmsItem(mapper.mapEntityToDbModel(smsItem))
    }

    override suspend fun deleteAllSmsItems() {
        smsListDao.deleteAllSmsItems()
    }
}