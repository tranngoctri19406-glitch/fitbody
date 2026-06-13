package com.example.fitbody.ui.adapter



import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.ImageView

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.example.fitbody.R

import com.example.fitbody.model.Trainer



class ChatListAdapter(

    private val trainers: List<Trainer>,

    private val onClick: (Trainer) -> Unit

) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {



// Lớp ViewHolder để quản lý các view trong item_chat_list.xml

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgAvatar: ImageView = view.findViewById(R.id.imgChatAvatar)

        val txtName: TextView = view.findViewById(R.id.txtChatName)

        val txtSpecialty: TextView = view.findViewById(R.id.txtChatSpecialty)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)

            .inflate(R.layout.item_chat_list, parent, false)

        return ViewHolder(view)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val trainer = trainers[position]



        holder.txtName.text = trainer.name

        holder.txtSpecialty.text = trainer.specialty



        // Load ảnh avatar từ drawable dựa trên tên ảnh trong Database

        val context = holder.itemView.context

        val imageResId = context.resources.getIdentifier(

            trainer.image, "drawable", context.packageName

        )



        if (imageResId != 0) {

            holder.imgAvatar.setImageResource(imageResId)

        } else {

            holder.imgAvatar.setImageResource(R.drawable.male) // Hình mặc định nếu không tìm thấy ảnh

        }



        // Sự kiện click vào item

        holder.itemView.setOnClickListener {

            onClick(trainer)

        }

    }



    override fun getItemCount(): Int = trainers.size

}