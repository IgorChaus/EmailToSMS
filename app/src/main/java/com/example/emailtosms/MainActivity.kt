package com.example.emailtosms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.emailtosms.model.email.EmailListRepositoryImpl
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            thread{
                val emailListRepositoryImpl = EmailListRepositoryImpl()
                emailListRepositoryImpl.getEmailList(
                    "alarm-parking@mail.ru",
                    "rQQz8Lq5zcTSdkPpb6W7",
                    "imap.mail.ru",
                    "995",
                    true
                )
            }
        }
    }

}