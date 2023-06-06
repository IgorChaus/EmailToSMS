package com.example.emailtosms.data.sms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_items")
data class SmsItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val phone: String,
    val message: String
)
