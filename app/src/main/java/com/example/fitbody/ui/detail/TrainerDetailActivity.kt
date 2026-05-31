package com.example.fitbody.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.adapter.WorkoutAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainerDetailActivity : AppCompatActivity() {

    private lateinit var recyclerWorkout: RecyclerView
    private lateinit var btnBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_trainer_detail)

        btnBack = findViewById(R.id.btnBack)

        val imgTrainer =
            findViewById<ImageView>(R.id.imgTrainer)

        val txtName =
            findViewById<TextView>(R.id.txtName)

        val txtSpecialty =
            findViewById<TextView>(R.id.txtSpecialty)

        val txtCalories =
            findViewById<TextView>(R.id.txtCalories)

        val txtMuscle =
            findViewById<TextView>(R.id.txtMuscle)

        val txtSchedule =
            findViewById<TextView>(R.id.txtSchedule)

        recyclerWorkout =
            findViewById(R.id.recyclerWorkout)

        btnBack.setOnClickListener {
            finish()
        }

        recyclerWorkout.layoutManager =
            LinearLayoutManager(this)

        val trainerId =
            intent.getIntExtra("trainer_id", 0)

        val name =
            intent.getStringExtra("trainer_name") ?: ""

        val specialty =
            intent.getStringExtra("trainer_specialty") ?: ""

        val image =
            intent.getStringExtra("trainer_image") ?: ""

        val calories =
            intent.getStringExtra("trainer_calories") ?: ""

        val muscle =
            intent.getStringExtra("trainer_muscle") ?: ""

        val schedule =
            intent.getStringExtra("trainer_schedule") ?: ""

        txtName.text = name
        txtSpecialty.text = specialty
        txtCalories.text = calories
        txtMuscle.text = muscle
        txtSchedule.text = schedule

        Glide.with(this)
            .load(image)
            .into(imgTrainer)

        loadWorkouts(trainerId)
    }

    private fun loadWorkouts(trainerId: Int) {
        RetrofitClient.instance
            .getWorkouts(trainerId)
            .enqueue(object : Callback<List<Workout>> {

                override fun onResponse(
                    call: Call<List<Workout>>,
                    response: Response<List<Workout>>
                ) {
                    if (response.isSuccessful) {
                        recyclerWorkout.adapter =
                            WorkoutAdapter(
                                response.body() ?: emptyList()
                            )
                    }
                }

                override fun onFailure(
                    call: Call<List<Workout>>,
                    t: Throwable
                ) {
                }
            })
    }
}