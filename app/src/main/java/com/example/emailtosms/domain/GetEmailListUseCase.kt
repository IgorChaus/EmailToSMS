package com.example.emailtosms.domain

    class GetEmailListUseCase(private val emailListRepository: EmailListRepository) {
        fun getEmailList(): List<EmailItem> = emailListRepository.getEmailList()
    }
