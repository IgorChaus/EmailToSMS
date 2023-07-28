package com.example.emailtosms.presentation.sms.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emailtosms.domain.sms.SmsItem

object SmsItemDiffCallBack: DiffUtil.ItemCallback<SmsItem>() {
    override fun areItemsTheSame(oldItem: SmsItem, newItem: SmsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SmsItem, newItem: SmsItem): Boolean {
        return oldItem == newItem
    }
}