package com.example.fitbody.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.Message

class ChatAdapter(
    private val messages: List<Message>,
    private val currentUserId: Int
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT     = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtContent: TextView = view.findViewById(R.id.txtMessageContent)
        val txtTime: TextView    = view.findViewById(R.id.txtMessageTime)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender_id == currentUserId)
            VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT)
            R.layout.item_message_sent
        else
            R.layout.item_message_received
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        holder.txtContent.text = msg.content
        holder.txtTime.text    = msg.timestamp
    }

    override fun getItemCount() = messages.size
}