package com.example.emailtosms.presentation.sms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.emailtosms.databinding.ItemSmsBinding
import com.example.emailtosms.domain.sms.SmsItem

class SmsListAdapter: ListAdapter<SmsItem, SmsItemViewHolder>(SmsItemDiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsItemViewHolder {
        val binding = ItemSmsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SmsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmsItemViewHolder, position: Int) {
        val smsItem = getItem(position)
        with(holder.binding) {
            tvDate.text = smsItem.date
            tvMessage.text = smsItem.message
            tvPhone.text = smsItem.phone
        }
    }

}