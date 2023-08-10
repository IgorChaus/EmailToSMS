package com.example.emailtosms.data.di

import android.app.Application
import com.example.emailtosms.presentation.email.EmailListScreen
import com.example.emailtosms.presentation.settings.SettingsFragment
import com.example.emailtosms.presentation.sms.SmsListScreen
import dagger.BindsInstance
import dagger.Component

@Component(modules = [
    DataModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(fragment: SmsListScreen)

    fun inject(fragment: EmailListScreen)

    fun inject(fragment: SettingsFragment)


    @Component.Factory
    interface ApplicationComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}