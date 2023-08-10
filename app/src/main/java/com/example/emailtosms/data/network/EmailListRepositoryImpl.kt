package com.example.emailtosms.data.network

import com.example.emailtosms.data.mapper.MapperEmail
import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailListRepository
import com.example.emailtosms.domain.email.EmailResponse
import java.util.*
import javax.inject.Inject
import javax.mail.*
import javax.mail.search.FlagTerm

class EmailListRepositoryImpl @Inject constructor(private val mapperEmail: MapperEmail): EmailListRepository {

    override suspend fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        token: String,
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

                for(message in messageList){
                    val subject = message.subject.trim()
                    if (subject == token) {
                        val item = mapperEmail.mapEmailMessageToEmailItem(message)
                        emailList.add(item)
                        if (isDeleted) {
                            message.setFlag(Flags.Flag.DELETED, true)
                        }
                    }
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

    override suspend fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
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
                folder.open(Folder.READ_ONLY)
                val messageList = folder.messages
                for (message in messageList){
                    val item = mapperEmail.mapEmailMessageToEmailItem(message)
                    emailList.add(item)
                }
                emailList.reverse()
                folder.close(false)
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
            EmailResponse(emailList, response)
            return EmailResponse(emailList, response)
        }
    }

    companion object {
        val OK = null
        private var LOCK = Any()
    }
}