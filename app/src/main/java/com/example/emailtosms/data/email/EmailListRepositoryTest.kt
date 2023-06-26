package com.example.emailtosms.data.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailListRepository
import com.example.emailtosms.domain.email.EmailResponse
import java.util.*

object EmailListRepositoryTest: EmailListRepository {

    private val emailResponseLD = MutableLiveData<EmailResponse>()

    private val messageList = arrayListOf<EmailItem>()

    init {
        messageList.add(
            EmailItem(
                Date(2023, 5, 16, 10, 30, 0),
                "doktrina@inbox.ru",
                "5791",
                "+79114509981 Ваш заказ №236 готов к выдаче"
            )
        )
        messageList.add(
            EmailItem(
                Date(2023, 4, 30, 11, 31, 14),
                "1c@licence.ru",
                "Ваш счет",
                "Здравствуйте, к письму прикреплен счет"
            )
        )
        messageList.add(
            EmailItem(
                Date(2023, 4, 20, 23, 1, 2),
                "igor_chaus@mail.ru",
                "Ваш счет",
                "Здравствуйте, к письму прикреплен счет"
            )
        )
        messageList.add(
            EmailItem(
                Date(2023, 3, 8, 3, 11, 48),
                "doktrina@inbox.ru",
                "5791",
                "+79114509981 Ваш заказ №251 готов к выдаче"
            )
        )
    }

    override fun getEmailListWithToken(
        user: String,
        password: String,
        host: String,
        port: String,
        isDeleted: Boolean
    ): EmailResponse {

        val emailList = arrayListOf<EmailItem>()
        val response: String? = EmailListRepositoryImpl.OK

        var count = 0
        while (count < messageList.size) {
            val message = messageList[count]
            val subject = message.subject.trim()
            if (subject == "5791") {
                val item = message
                emailList.add(item)
                if (isDeleted) {
                    messageList.remove(item)
                }
            }
            count++
        }
        return EmailResponse(emailList, response)
    }

    override fun getEmailList(
        user: String,
        password: String,
        host: String,
        port: String
    ): LiveData<EmailResponse> {
        emailResponseLD.value = EmailResponse(messageList, OK)
        return emailResponseLD
    }

        val OK = null

}