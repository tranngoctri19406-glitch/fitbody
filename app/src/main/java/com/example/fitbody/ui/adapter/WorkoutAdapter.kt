package com.example.fitbody.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.model.Workout
import com.example.fitbody.ui.detail.WorkoutDetailActivity

class WorkoutAdapter(

    private val list: List<Workout>

) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtWorkoutName: TextView =
            view.findViewById(R.id.txtWorkoutName)

        val txtSets: TextView =
            view.findViewById(R.id.txtSets)

        val txtReps: TextView =
            view.findViewById(R.id.txtReps)

        val txtMuscleGroup: TextView =
            view.findViewById(R.id.txtMuscleGroup)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_workout,
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

        val workout =
            list[position]

        holder.txtWorkoutName.text =
            workout.workout_name

        holder.txtSets.text =
            "Sets: ${workout.sets_count}"

        holder.txtReps.text =
            "Reps: ${workout.reps_count}"

        holder.txtMuscleGroup.text =
            "Nhóm cơ: ${workout.muscle_group}"

        holder.itemView.setOnClickListener {

            val intent =
                Intent(
                    holder.itemView.context,
                    WorkoutDetailActivity::class.java
                )

            intent.putExtra("workout_name", workout.workout_name)
            intent.putExtra("sets", workout.sets_count)
            intent.putExtra("reps", workout.reps_count)
            intent.putExtra("muscle", workout.muscle_group)
            intent.putExtra("video_url", workout.video_url)

            holder.itemView.context
                .startActivity(intent)
        }
    }
}