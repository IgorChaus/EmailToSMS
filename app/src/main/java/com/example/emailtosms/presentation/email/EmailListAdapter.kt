package com.example.emailtosms.presentation.email

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emailtosms.R
import com.example.emailtosms.domain.email.EmailItem
import java.text.SimpleDateFormat
import java.util.*


class EmailListAdapter: RecyclerView.Adapter<EmailListAdapter.EmailItemViewHolder>() {

    var emailList = listOf<EmailItem>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    class EmailItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val tvAddress = view.findViewById<TextView>(R.id.tv_address)
        val tvSubject = view.findViewById<TextView>(R.id.tv_subject)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_email,
            parent,
            false
        )
        return EmailItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailItemViewHolder, position: Int) {
        val emailItem =emailList[position]
        val dateFormat = SimpleDateFormat("dd.MM", Locale("ru", "RU"))
        holder.tvDate.text = emailItem.date?.let { dateFormat.format(it) }
        holder.tvAddress.text = emailItem.address
        holder.tvSubject.text = emailItem.subject
        holder.tvMessage.text = emailItem.message
    }

    override fun getItemCount() = emailList.size
}