package com.example.emailtosms.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SmsItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun smsListDao(): SmsListDao

    companion object{
        private var INSTANCE: AppDataBase? = null
        private var LOCK = Any()
        private const val DB_NAME = "sms_item.db"

        fun getInstance(context: Context): AppDataBase {

            INSTANCE?.let{
                return it
            }

            synchronized(LOCK){
                INSTANCE?.let{
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = db
                return db
            }
        }
    }
}
