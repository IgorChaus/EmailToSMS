package com.example.emailtosms.presentation.sms

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emailtosms.R
import com.example.emailtosms.domain.sms.SmsItem

class SmsListAdapter: RecyclerView.Adapter<SmsListAdapter.SmsItemViewHolder>() {

    var smsList = listOf<SmsItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    class SmsItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvPhone = view.findViewById<TextView>(R.id.tv_address)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val tvMessage = view.findViewById<TextView>(R.id.tv_subject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_sms,
            parent,
            false
        )
        return SmsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsItemViewHolder, position: Int) {
        val smsItem =smsList[position]
        holder.tvDate.text = smsItem.date
        holder.tvMessage.text = smsItem.message
        holder.tvPhone.text = smsItem.phone
    }

    override fun getItemCount(): Int {
        Log.i("MyTag","smsList.size: ${smsList.size}")
        return smsList.size
    }

}