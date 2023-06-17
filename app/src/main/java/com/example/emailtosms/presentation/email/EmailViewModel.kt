package com.example.emailtosms.presentation.email

import androidx.lifecycle.ViewModel
import com.example.emailtosms.data.email.EmailListRepositoryTest
import com.example.emailtosms.domain.email.GetEmailListUseCase

class EmailViewModel: ViewModel() {
    private val repository = EmailListRepositoryTest()

    private val getEmailListUseCase = GetEmailListUseCase(repository)

    val emailResponse = getEmailListUseCase.getEmailList(
        "alarm-parking@mail.ru",
        "chusya12",
        "imap.mail.ru",
        "995"
    )
}