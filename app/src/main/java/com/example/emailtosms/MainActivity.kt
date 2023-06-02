package com.example.emailtosms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emailtosms.domain.EmailListRepositoryImpl

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val emailListRepositoryImpl = EmailListRepositoryImpl()
    }
}