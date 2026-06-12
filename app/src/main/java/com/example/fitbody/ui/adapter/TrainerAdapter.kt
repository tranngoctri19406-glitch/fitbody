package com.example.fitbody.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.Trainer

class TrainerAdapter(

    private val list: List<Trainer>,

    private val onClick: (Trainer) -> Unit,

    private val onFavorite: (Trainer) -> Unit,
    
    private val onLike: (Trainer) -> Unit

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

        val imgLike: ImageView =
            view.findViewById(R.id.imgLike)

        val txtFavoriteCount: TextView =
            view.findViewById(R.id.txtFavoriteCount)
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

        val trainer = list[position]

        var isFavorite = false

        holder.txtName.text = trainer.name
        holder.txtSpecialty.text = trainer.specialty
        holder.txtCalories.text = trainer.calories
        holder.txtFavoriteCount.text = "${trainer.likeCount} lượt thích"

        val imageResId =
            holder.itemView.context.resources.getIdentifier(
                trainer.image,
                "drawable",
                holder.itemView.context.packageName
            )

        if (imageResId != 0) {
            holder.imgTrainer.setImageResource(imageResId)
        } else {
            holder.imgTrainer.setImageResource(R.drawable.male)
        }

        holder.imgFavorite.setImageResource(
            android.R.drawable.btn_star_big_on
        )

        holder.imgFavorite.setColorFilter(Color.WHITE)

        holder.imgFavorite.setOnClickListener {

            isFavorite = !isFavorite

            if (isFavorite) {
                holder.imgFavorite.setColorFilter(Color.RED)
                onFavorite(trainer)
            } else {
                holder.imgFavorite.setColorFilter(Color.WHITE)
            }
        }

        holder.imgLike.setOnClickListener {
            onLike(trainer)
        }

        holder.itemView.setOnClickListener {
            onClick(trainer)
        }
    }
}