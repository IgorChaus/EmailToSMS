package com.example.emailtosms.domain.sms

import com.example.emailtosms.model.SmsItemDbModel

class SmsItemMapper {

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