package com.example.emailtosms.domain.email

import com.example.emailtosms.domain.email.EmailItem
import com.example.emailtosms.domain.email.EmailListRepository

class GetEmailListUseCase(private val emailListRepository: EmailListRepository) {
        fun getEmailList(): List<EmailItem> = emailListRepository.getEmailList()
    }
