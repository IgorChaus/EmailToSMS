package com.example.emailtosms.data.di

import androidx.lifecycle.ViewModel
import com.example.emailtosms.presentation.email.EmailViewModel
import com.example.emailtosms.presentation.settings.SettingsViewModel
import com.example.emailtosms.presentation.sms.SmsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @ViewModelKey(EmailViewModel::class)
    @IntoMap
    @Binds
    fun bindEmailViewModel(impl: EmailViewModel): ViewModel

    @ViewModelKey(SmsViewModel::class)
    @IntoMap
    @Binds
    fun bindSmsViewModel(impl: SmsViewModel): ViewModel

    @ViewModelKey(SettingsViewModel::class)
    @IntoMap
    @Binds
    fun bindSettingsViewModel(impl: SettingsViewModel): ViewModel

}