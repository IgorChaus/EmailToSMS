package com.example.emailtosms.presentation

import android.app.Application
import androidx.work.Configuration
import com.example.emailtosms.data.di.DaggerApplicationComponent
import com.example.emailtosms.data.workers.RefreshEmailWorkerFactory
import javax.inject.Inject

class EmailToListApp: Application(), Configuration.Provider{
    val component by lazy{
       DaggerApplicationComponent.factory().create(this)
    }
    @Inject
    lateinit var workerFactory: RefreshEmailWorkerFactory

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}