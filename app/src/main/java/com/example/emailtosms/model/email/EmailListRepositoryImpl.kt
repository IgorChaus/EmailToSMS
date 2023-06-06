package com.example.emailtosms.model.email

import android.os.Build
import android.text.Html
import android.util.Log
import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailResponse
import java.util.*
import javax.mail.*
import javax.mail.search.FlagTerm

class EmailListRepositoryImpl {

    fun getEmailList(
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
                    if (message.subject.trim() == "5791") {
                        val item = mapEmailMessageToEmailItem(message)
                        emailList.add(item)
                    }
                    if (isDeleted) {
                        message.setFlag(Flags.Flag.DELETED, true)
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

    private fun mapEmailMessageToEmailItem(message: Message): EmailItem {
            val getMulti = GetMulti()
            var emailMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getMulti.getTextFromMessage(message), Html.FROM_HTML_MODE_LEGACY)
                    .toString()
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(getMulti.getTextFromMessage(message)).toString()
            }
            emailMessage = emailMessage.trim()
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
            return EmailItem(phone, smsMessage)
    }

    companion object {
        val OK = null
        private var LOCK = Any()
    }
}