package com.example.emailtosms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emailtosms.presentation.SmsListScreen
import com.example.emailtosms.presentation.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.sms_screen -> {
                    launchListScreen()
                    true
                }
                R.id.email_screen -> {
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

    fun launchSettingsScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, SettingsFragment.getInstance())
            .addToBackStack(null)
            .commit()
    }

    fun launchListScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, SmsListScreen.getInstance())
            .addToBackStack(null)
            .commit()
    }


}



