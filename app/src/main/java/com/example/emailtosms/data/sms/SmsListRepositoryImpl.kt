package com.example.emailtosms.data.sms

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.emailtosms.domain.sms.SmsItem
import com.example.emailtosms.domain.sms.SmsItemMapper
import com.example.emailtosms.domain.sms.SmsListRepository

class SmsListRepositoryImpl(context: Context): SmsListRepository {
    private val smsListDao = AppDataBase.getInstance(context).smsListDao()
    private val mapper = SmsItemMapper()

    override fun getSmsList(limit: Int): LiveData<List<SmsItem>> = Transformations.map(
        smsListDao.getSmsList(limit)){
        mapper.mapListDbModelToListEntity(it)
    }


    override suspend fun addSmsItem(smsItem: SmsItem) {
        smsListDao.addSmsItem(mapper.mapEntityToDbModel(smsItem))
    }

    override suspend fun deleteAllSmsItems() {
        smsListDao.deleteAllSmsItems()
    }
}