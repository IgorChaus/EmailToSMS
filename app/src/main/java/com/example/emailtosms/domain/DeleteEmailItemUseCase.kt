package com.example.emailtosms.domain

class DeleteEmailItemUseCase(private val emailListRepository: EmailListRepository) {
    fun deleteEmailItem(emailItem: EmailItem){
        emailListRepository.deleteEmailItem(emailItem)
    }
}