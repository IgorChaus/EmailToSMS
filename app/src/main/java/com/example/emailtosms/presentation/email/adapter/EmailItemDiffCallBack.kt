package com.example.emailtosms.presentation.email.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.emailtosms.domain.email.EmailItem

object EmailItemDiffCallBack: DiffUtil.ItemCallback<EmailItem>() {
    override fun areItemsTheSame(oldItem: EmailItem, newItem: EmailItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EmailItem, newItem: EmailItem): Boolean {
        return oldItem == newItem
    }
}