package com.example.emailtosms.presentation.email.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.emailtosms.databinding.ItemEmailBinding
import com.example.emailtosms.domain.email.EmailItem
import java.text.SimpleDateFormat
import java.util.*


class EmailListAdapter: ListAdapter<EmailItem, EmailItemViewHolder>(EmailItemDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailItemViewHolder {

        val binding = ItemEmailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmailItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailItemViewHolder, position: Int) {
        val emailItem = getItem(position)
        with(holder.binding) {
            tvDate.text = emailItem.date
            tvAddress.text = emailItem.address
            tvSubject.text = emailItem.subject
            tvMessage.text = emailItem.message
        }
    }

}