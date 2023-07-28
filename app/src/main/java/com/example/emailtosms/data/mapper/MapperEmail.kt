package com.example.emailtosms.data.mapper

import android.os.Build
import android.text.Html
import com.example.emailtosms.data.network.GetMulti
import com.example.emailtosms.domain.email.EmailItem
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress

class MapperEmail {

    fun mapEmailMessageToEmailItem(message: Message): EmailItem {

        var address = ""
        try {
            message.from?.let {
                address = (it[0] as InternetAddress).address
            }
        } catch (e: MessagingException){
            address = ""
        }

        var subject: String
        try {
            subject = message.subject.trim()
        } catch (e: MessagingException){
            subject = ""
        }

        var date: Date?
        try {
            date = message.sentDate
        } catch (e: MessagingException){
            date = null
        }

        var emailMessage: String
        try {
            val getMulti = GetMulti()
            emailMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getMulti.getTextFromMessage(message), Html.FROM_HTML_MODE_LEGACY)
                    .toString()
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(getMulti.getTextFromMessage(message)).toString()
            }
        } catch (e: MessagingException){
            emailMessage = ""
        }

        return EmailItem(date, address, subject,  emailMessage)
    }
}