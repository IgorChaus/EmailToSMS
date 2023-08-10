package com.example.emailtosms.data.mapper

import com.example.emailtosms.data.database.SmsItemDbModel
import com.example.emailtosms.domain.sms.SmsItem
import javax.inject.Inject

class MapperSmsItemToEntity @Inject constructor(){

    fun mapEntityToDbModel(smsItem: SmsItem) = SmsItemDbModel(
        id = smsItem.id,
        date = smsItem.date,
        phone = smsItem.phone,
        message = smsItem.message
    )

    fun mapDbModelToEntity(smsItemDbModel: SmsItemDbModel) = SmsItem(
        id = smsItemDbModel.id,
        date = smsItemDbModel.date,
        phone = smsItemDbModel.phone,
        message = smsItemDbModel.message
    )

    fun mapListDbModelToListEntity(list: List<SmsItemDbModel>) = list.map {
        mapDbModelToEntity(it)

    }
}