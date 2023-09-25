package com.example.emailtosms.data.database

import com.example.emailtosms.data.mapper.MapperSmsItemToEntity
import com.example.emailtosms.domain.sms.SmsItem
import com.example.emailtosms.domain.sms.SmsListRepository
import javax.inject.Inject

class SmsListRepositoryImpl @Inject constructor(
    private val smsListDao: SmsListDao,
    private val mapper: MapperSmsItemToEntity
): SmsListRepository {
//    private val smsListDao = AppDataBase.getInstance(context).smsListDao()
//    private val mapper = MapperSmsItemToEntity()

    override suspend fun getSmsList(limit: Int): List<SmsItem> =
        smsListDao.getSmsList(limit).map {
            mapper.mapDbModelToEntity(it)
        }

    override suspend fun addSmsItem(smsItem: SmsItem) {
        smsListDao.addSmsItem(mapper.mapEntityToDbModel(smsItem))
    }

    override suspend fun deleteAllSmsItems() {
        smsListDao.deleteAllSmsItems()
    }
}