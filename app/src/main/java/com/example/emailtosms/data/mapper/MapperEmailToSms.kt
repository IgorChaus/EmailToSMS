package com.example.emailtosms.data.mapper

import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.sms.SmsItem
import java.text.SimpleDateFormat
import java.util.*

class MapperEmailToSms {

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

        val dateFormat = SimpleDateFormat("dd.MM", Locale("ru", "RU"))
        val date = item.date?.let { dateFormat.format(it) } ?: ""

        var smsMessage = emailMessage.substring(spaceIndex).trim()
        smsMessage = smsMessage.replace("\n", "")

        return SmsItem(SmsItem.UNDEFIND_ID, date, phone, smsMessage)
    }
}