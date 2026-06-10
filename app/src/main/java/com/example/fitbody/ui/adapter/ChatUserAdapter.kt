package com.example.fitbody.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.ChatUser

class ChatUserAdapter(
    private val chatUserList: List<ChatUser>,
    private val onItemClick: (ChatUser) -> Unit
) : RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder>() {

    class ChatUserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val txtUserName: TextView =
            itemView.findViewById(R.id.txtUserName)

        val txtLastMessage: TextView =
            itemView.findViewById(R.id.txtLastMessage)

        val txtLastTime: TextView =
            itemView.findViewById(R.id.txtLastTime)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatUserViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_chat_user,
                    parent,
                    false
                )

        return ChatUserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatUserViewHolder,
        position: Int
    ) {
        val item = chatUserList[position]

        holder.txtUserName.text = item.name
        holder.txtLastMessage.text = item.last_message
        holder.txtLastTime.text = item.last_time

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return chatUserList.size
    }
}