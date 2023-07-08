package com.example.emailtosms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.emailtosms.presentation.email.EmailListScreen
import com.example.emailtosms.presentation.settings.SettingsFragment
import com.example.emailtosms.presentation.sms.SmsListScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.sms_screen -> {
                    launchSmsListScreen()
                    true
                }
                R.id.email_screen -> {
                    launchEmailListScreen()
                    true
                }
                R.id.settins_screen -> {
                    launchSettingsScreen()
                    true
                }
                else -> throw RuntimeException("Illegal choose")
            }
        }

    }

    private fun launchSettingsScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, SettingsFragment.getInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun launchSmsListScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, SmsListScreen.getInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun launchEmailListScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, EmailListScreen.getInstance())
            .addToBackStack(null)
            .commit()
    }

}



