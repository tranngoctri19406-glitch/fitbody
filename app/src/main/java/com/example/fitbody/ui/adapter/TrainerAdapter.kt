package com.example.fitbody.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.model.Trainer

class TrainerAdapter(

    private val list: List<Trainer>,

    private val onClick: (Trainer) -> Unit,

    private val onFavorite: (Trainer) -> Unit

) : RecyclerView.Adapter<TrainerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtName: TextView =
            view.findViewById(R.id.txtName)

        val txtSpecialty: TextView =
            view.findViewById(R.id.txtSpecialty)

        val txtCalories: TextView =
            view.findViewById(R.id.txtCalories)

        val imgTrainer: ImageView =
            view.findViewById(R.id.imgTrainer)

        val imgFavorite: ImageView =
            view.findViewById(R.id.imgFavorite)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_trainer,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val trainer =
            list[position]

        var isFavorite = false

        holder.txtName.text =
            trainer.name

        holder.txtSpecialty.text =
            trainer.specialty

        holder.txtCalories.text =
            trainer.calories

        Glide.with(holder.itemView.context)
            .load(trainer.image)
            .into(holder.imgTrainer)

        // Mặc định icon yêu thích màu trắng

        holder.imgFavorite.setImageResource(
            android.R.drawable.btn_star_big_on
        )

        holder.imgFavorite.setColorFilter(
            Color.WHITE
        )

        // Bấm lần 1: đỏ, bấm lần 2: trắng

        holder.imgFavorite.setOnClickListener {

            isFavorite = !isFavorite

            if (isFavorite) {

                holder.imgFavorite.setColorFilter(
                    Color.RED
                )

                onFavorite(trainer)

            } else {

                holder.imgFavorite.setColorFilter(
                    Color.WHITE
                )
            }
        }

        holder.itemView.setOnClickListener {

            onClick(trainer)
        }
    }
}