package com.hbeonlab.rms.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hbeonlab.rms.R
import com.hbeonlab.rms.bluetooth.Contstants
import java.util.*

class MessageListAdapter(var list: MutableList<MessageItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)

    private inner class SelfViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.message)
        var date: TextView = itemView.findViewById(R.id.date)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.message
            date.text = formatter.format(recyclerViewModel.date)
        }
    }

    private inner class OtherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.message)
        var date: TextView = itemView.findViewById(R.id.date)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            message.text = recyclerViewModel.message
            date.text = formatter.format(recyclerViewModel.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return SelfViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_message_self, parent, false)
            )
        }
        return OtherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_message_other, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].sender == Contstants.SELF) {
            (holder as SelfViewHolder).bind(position)
        } else {
            (holder as OtherViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].sender
    }

    fun update(newMessage: MessageItem){
        this.list.add(newMessage)
        notifyItemInserted(list.size)
    }
}

data class MessageItem(val id:Int,val profileUrl: String, val message: String, val sender : Int, val date : Date)

