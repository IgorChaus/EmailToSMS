package com.example.emailtosms.data.di

import android.app.Application
import com.example.emailtosms.data.database.AppDataBase
import com.example.emailtosms.data.database.SmsListDao
import com.example.emailtosms.data.database.SmsListRepositoryImpl
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.domain.email.EmailListRepository
import com.example.emailtosms.domain.sms.SmsListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindEmailListRepository(impl: EmailListRepositoryImpl): EmailListRepository

    @Binds
    fun bindSmsListRepository(impl: SmsListRepositoryImpl): SmsListRepository

    companion object{

        @Provides
        fun provideSmsListDao(application: Application): SmsListDao{
            return AppDataBase.getInstance(application).smsListDao()
        }
    }
}