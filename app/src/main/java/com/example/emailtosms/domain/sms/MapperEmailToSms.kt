package com.example.emailtosms.domain.sms

class MapperEmailToSms {

    fun mapEmailMessageToSmsMessage(message: String): Map<String,String>{
        val emailMessage = message.trim()
        val spaceIndex = emailMessage.indexOf(" ")
        var phone = emailMessage.substring(0, spaceIndex)

        val re = Regex("[^0-9]")
        if (phone[0] == '+') {
            phone = re.replace(phone, "")
            phone = "+ $phone"
        } else {
            phone = re.replace(phone, "")
        }

        var smsMessage = emailMessage.substring(spaceIndex).trim()
        smsMessage = smsMessage.replace("\n", "")

        return mapOf("phone" to phone, "message" to smsMessage)
    }
}