package com.example.emailtosms.data.sms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.emailtosms.domain.sms.SmsItem
import com.example.emailtosms.domain.sms.SmsListRepository
import kotlin.random.Random

class SmsListRepositoryTest: SmsListRepository {

    private val smsListLD = MutableLiveData<List<SmsItem>>()

    private val smsList = arrayListOf<SmsItem>()

    private var autoIncrementId = 0

    init{
        for(i in 0 until 20){
            val order = Random.nextInt(1,500)
            val phone1 = Random.nextInt(1000,9999)
            val phone2 = Random.nextInt(1000,9999)
            val smsItem = SmsItem(
                i,
                "$i.03.2023",
                "+791$phone1$phone2",
                "Ваш заказ N$order готов к выдаче"
            )
            addSmsItem(smsItem)
        }
    }

    override fun getSmsList(): LiveData<List<SmsItem>> {
        return smsListLD
    }

    override fun addSmsItem(smsItem: SmsItem) {
        smsList.add(smsItem)
        updateList()
    }

    override fun deleteAllSmsItems() {
        smsList.clear()
        updateList()
    }

    fun updateList(){
        smsListLD.value = smsList.toList()
    }
}