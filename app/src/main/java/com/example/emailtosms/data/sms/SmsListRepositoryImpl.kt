package com.example.emailtosms.data.sms

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.emailtosms.domain.sms.SmsItem
import com.example.emailtosms.domain.sms.SmsItemMapper
import com.example.emailtosms.domain.sms.SmsListRepository
import com.example.emailtosms.data.sms.AppDataBase

class SmsListRepositoryImpl(application: Application): SmsListRepository {
    private val smsListDao = AppDataBase.getInstance(application).smsListDao()
    private val mapper = SmsItemMapper()

    override fun getSmsList(): LiveData<List<SmsItem>> = Transformations.map(
        smsListDao.getSmsList()){
        mapper.mapListDbModelToListEntity(it)
    }


    override suspend fun addSmsItem(smsItem: SmsItem) {
        smsListDao.addSmsItem(mapper.mapEntityToDbModel(smsItem))
    }

    override suspend fun deleteAllSmsItems() {
        smsListDao.deleteAllSmsItems()
    }
}