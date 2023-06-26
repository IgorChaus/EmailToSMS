package com.example.emailtosms.data.email

import android.os.Build
import android.text.Html
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailListRepository
import com.example.emailtosms.domain.email.EmailResponse
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.search.FlagTerm

class EmailListRepositoryImpl: EmailListRepository {
    private val emailResponseLD = MutableLiveData<EmailResponse>()

    override fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        isDeleted: Boolean
    ): EmailResponse {

        synchronized(LOCK){
            val emailList = arrayListOf<EmailItem>()
            var response: String? = OK

            val properties = Properties()
            properties["mail.imap.host"] = host
            properties["mail.imap.port"] = port
            properties["mail.imap.starttls.enable"] = "true"
            try {
                val store = Session.getDefaultInstance(properties).getStore("imaps")
                store.connect(host, user, password)
                val folder = store.getFolder("INBOX")
                folder.open(Folder.READ_WRITE)

                val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
                val messageList = folder.search(ft)
                Log.i("MyTag", "Сообщений в ящике:" + messageList.size)

                var count = 0
                while (count < messageList.size) {
                    val message = messageList[count]
                    val subject = message.subject.trim()
                    if (subject == "5791") {
                        val item = mapEmailMessageToEmailItem(message)
                        emailList.add(item)
                        if (isDeleted) {
                            message.setFlag(Flags.Flag.DELETED, true)
                        }
                    }
                    count++
                }
                folder.close(true)
                store.close()
            } catch (e: AuthenticationFailedException){
                response = "AuthenticationFailedException $e"
            } catch (e: NoSuchProviderException) {
                response = "NoSuchProviderException $e"
            } catch (e: MessagingException) {
                response = "MessagingException $e"
            } catch (e: Exception) {
                response = "Exception in EmailListRepositoryImpl $e"
            }
            return EmailResponse(emailList, response)
        }
    }

    override fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
    ): LiveData<EmailResponse> {

        synchronized(LOCK){
            val emailList = arrayListOf<EmailItem>()
            var response: String? = OK

            val properties = Properties()
            properties["mail.imap.host"] = host
            properties["mail.imap.port"] = port
            properties["mail.imap.starttls.enable"] = "true"
            try {
                val store = Session.getDefaultInstance(properties).getStore("imaps")
                store.connect(host, user, password)
                val folder = store.getFolder("INBOX")
                folder.open(Folder.READ_WRITE)

                val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
                val messageList = folder.search(ft)
                Log.i("MyTag", "Сообщений в ящике:" + messageList.size)

                var count = 0
                while (count < messageList.size) {
                    val message = messageList[count]
                    val item = mapEmailMessageToEmailItem(message)
                    emailList.add(item)
                    count++
                }
                folder.close(true)
                store.close()
            } catch (e: AuthenticationFailedException){
                response = "AuthenticationFailedException $e"
            } catch (e: NoSuchProviderException) {
                response = "NoSuchProviderException $e"
            } catch (e: MessagingException) {
                response = "MessagingException $e"
            } catch (e: Exception) {
                response = "Exception in EmailListRepositoryImpl $e"
            }
            emailResponseLD.value = EmailResponse(emailList, response)
            return emailResponseLD
        }
    }

    private fun mapEmailMessageToEmailItem(message: Message): EmailItem {

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


    companion object {
        val OK = null
        private var LOCK = Any()
    }
}