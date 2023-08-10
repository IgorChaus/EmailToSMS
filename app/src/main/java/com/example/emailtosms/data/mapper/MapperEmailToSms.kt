package com.example.emailtosms.data.mapper

import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.sms.SmsItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MapperEmailToSms @Inject constructor(){

    fun mapEmailItemToSmsItem(item: EmailItem): SmsItem {
        val emailMessage = item.message.trim()
        val spaceIndex = emailMessage.indexOf(" ")
        var phone = emailMessage.substring(0, spaceIndex)

        val re = Regex("[^0-9]")
        if (phone[0] == '+') {
            phone = re.replace(phone, "")
            phone = "+ $phone"
        } else {
            phone = re.replace(phone, "")
        }

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM", Locale("ru", "RU"))
        val date = dateFormat.format(currentDate)

        var smsMessage = emailMessage.substring(spaceIndex).trim()
        smsMessage = smsMessage.replace("\n", "")

        return SmsItem(SmsItem.UNDEFIND_ID, date, phone, smsMessage)
    }


}