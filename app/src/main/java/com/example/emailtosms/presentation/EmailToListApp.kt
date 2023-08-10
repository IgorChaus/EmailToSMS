package com.example.emailtosms.presentation

import android.app.Application
import com.example.emailtosms.data.di.DaggerApplicationComponent

class EmailToListApp: Application() {
    val component by lazy{
       DaggerApplicationComponent.factory().create(this)
    }
}