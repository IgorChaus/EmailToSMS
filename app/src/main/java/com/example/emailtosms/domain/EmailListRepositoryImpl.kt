package com.example.emailtosms.domain

import android.os.Build
import android.text.Html
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.search.FlagTerm
import kotlin.concurrent.thread

class EmailListRepositoryImpl {

    fun openStore(user: String, password: String, host: String, port: String): Store{
        val properties = Properties()
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        properties["mail.imap.starttls.enable"] = "true"

        val store = Session.getDefaultInstance(properties).getStore("imaps")
        store.connect(host, user, password)

        return store
    }

    fun openFolder(store: Store): Folder{
        val emailFolder = store.getFolder("INBOX")
        emailFolder.open(Folder.READ_WRITE)
        return emailFolder
    }

    fun getMessageList(emailFolder: Folder): Array<Message>{
        val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
        val messageList = emailFolder.search(ft)
        Log.i("MyTag", "Сообщений в ящике:" + messageList.size)
        return messageList
    }

    fun deleteMessage(message: Message) =
        message.setFlag(Flags.Flag.DELETED, true)




    fun checkEmail(user: String, password: String, host: String, port: String) {
        val emailList = arrayListOf<EmailItem>()

        val properties = Properties()
        properties["mail.imap.host"] = host
        properties["mail.imap.port"] = port
        properties["mail.imap.starttls.enable"] = "true"

        thread {
            try {
                Log.i("MyTag", "T1")
                val store = Session.getDefaultInstance(properties).getStore("imaps")
                Log.i("MyTag", "T2")
                store.connect(host, user, password)
                val emailFolder = store.getFolder("INBOX")
                emailFolder.open(Folder.READ_WRITE)

                val ft = FlagTerm(Flags(Flags.Flag.SEEN), false)
                val messageList = emailFolder.search(ft)
                Log.i("MyTag", "Сообщений в ящике:" + messageList.size)

                val getMulti = GetMulti()
                var count = 1
                while (count <= messageList.size) {
                    val message = messageList[count]
                    val emailSubject = message.getSubject().trim()
                    var emailMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(getMulti.getText(message), Html.FROM_HTML_MODE_LEGACY)
                            .toString()
                    } else {
                        Html.fromHtml(getMulti.getText(message)).toString()
                    }
                    message.setFlag(Flags.Flag.DELETED, true)

                    if (emailSubject.equals("5791")) {
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
                        emailList.add(EmailItem(phone, smsMessage))
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
    }

}