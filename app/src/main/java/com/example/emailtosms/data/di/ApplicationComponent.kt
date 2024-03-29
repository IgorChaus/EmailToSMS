package com.example.emailtosms.data.di

import android.app.Application
import com.example.emailtosms.presentation.EmailToListApp
import com.example.emailtosms.presentation.email.EmailListScreen
import com.example.emailtosms.presentation.settings.SettingsFragment
import com.example.emailtosms.presentation.sms.SmsListScreen
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    DataModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(fragment: SmsListScreen)

    fun inject(fragment: EmailListScreen)

    fun inject(fragment: SettingsFragment)

    fun inject(application: EmailToListApp)


    @Component.Factory
    interface ApplicationComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}