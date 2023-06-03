package com.example.emailtosms.domain

import android.os.Build
import android.text.Html
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.search.FlagTerm
import kotlin.concurrent.thread

class EmailListRepositoryImpl {

    fun checkEmail(
        user: String,
        password: String,
        host: String,
        port: String,
        isDeleted: Boolean
    ):List<EmailItem>{

        val emailList = arrayListOf<EmailItem>()

        val properties = Properties()
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        properties["mail.imap.starttls.enable"] = "true"

        thread {
            try {
                val store = Session.getDefaultInstance(properties).getStore("imaps")
                store.connect(host, user, password)
                val emailFolder = store.getFolder("INBOX")
                emailFolder.open(Folder.READ_WRITE)

                val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
                val messageList = emailFolder.search(ft)
                Log.i("MyTag", "Сообщений в ящике:" + messageList.size)

                var count = 1
                while (count <= messageList.size) {
                    val message = messageList[count]
                    if (message.subject.trim() == "5791") {
                        emailList.add(mapEmailMessageToEmailItem(message))
                    }
                    if(isDeleted) {
                        message.setFlag(Flags.Flag.DELETED, true)
                    }
                    count++
                }
                emailFolder.close(true)
                store.close()

            } catch (e: NoSuchProviderException) {
                Log.i("MyTag", "NoSuchProviderException $e")
            } catch (e: MessagingException) {
                Log.i("MyTag", "MessagingException $e")
            } catch (e: Exception) {
                Log.i("MyTag", "Exception in EmailListRepositoryImpl $e")
            }
        }
        return emailList
    }

    private fun mapEmailMessageToEmailItem(message: Message): EmailItem{
            val getMulti = GetMulti()
            var emailMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(getMulti.getText(message), Html.FROM_HTML_MODE_LEGACY)
                    .toString()
            } else {
                Html.fromHtml(getMulti.getText(message)).toString()
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

}