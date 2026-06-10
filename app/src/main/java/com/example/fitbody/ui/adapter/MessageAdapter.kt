package com.example.fitbody.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.Message

class MessageAdapter(
    private val messageList: List<Message>,
    private val currentUserId: Int
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtMessage: TextView =
            itemView.findViewById(R.id.txtMessage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)

        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int
    ) {
        val item = messageList[position]

        if (item.sender_id == currentUserId) {
            holder.txtMessage.text = "Bạn: ${item.message}"
            holder.txtMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        } else {
            holder.txtMessage.text = "PT: ${item.message}"
            holder.txtMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}