package com.example.emailtosms.domain

import android.os.Build
import android.text.Html
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.search.FlagTerm

class EmailListRepositoryImpl {

    fun checkEmail(user: String, password: String, host: String, port: String) {

        val properties = Properties()
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        properties["mail.imap.starttls.enable"] = "true"

        try {

            val emailSession = Session.getDefaultInstance(properties)
            val store = emailSession.getStore("imaps")
            store.connect(host, user, password)
            val emailFolder = store.getFolder("INBOX")
            emailFolder.open(Folder.READ_WRITE)

            val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
            val messages = emailFolder.search(ft)
            Log.i("MyTag", "Сообщений в ящике:" + messages.size)

            val getMulti = GetMulti()
            var count = 1
            while (count <= messages.size) {
                val message = messages[count]
                val subject = message.getSubject().trim()
                val froms = message.getFrom()
                val email: String? = (froms[0] as InternetAddress).address
                val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(getMulti.getText(message), Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    Html.fromHtml(getMulti.getText(message)).toString()
                }
                message.setFlag(Flags.Flag.DELETED, true)

                if (subject.equals("5791")) {
                    val text = text.trim()
                    val space = text.indexOf(" ")
                    var phone = text.substring(0, space)

                    val re = Regex("[^0-9]")
                    if (phone[0] == '+') {
                        phone = re.replace(phone, "")
                        phone = "+ $phone"
                    } else {
                        phone = re.replace(phone, "")
                    }

                    var messageText = text.substring(space).trim()
                    messageText = messageText.replace("\n", "")
                }
                count++
            }
            emailFolder.close(true)
            store.close()

        }catch(e: NoSuchProviderException){
            Log.i("MyTag", "Error in NoSuchProviderException. +$e")
        }catch(e: MessagingException) {
            Log.i("MyTag", "Error in MessagingException. +$e")
        }catch (e: Exception){
            Log.i("MyTag", "Error in EmailListRepositoryImpl. +$e")
        }
    }
}