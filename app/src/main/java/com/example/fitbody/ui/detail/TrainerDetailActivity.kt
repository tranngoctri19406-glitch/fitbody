package com.example.fitbody.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.adapter.WorkoutAdapter
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.model.Workout
import com.example.fitbody.ui.ChatActivity
import android.widget.ImageButton
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
            onBackPressedDispatcher.onBackPressed()
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

        val imageResId = resources.getIdentifier(image, "drawable", packageName)
        if (imageResId != 0) {
            Glide.with(this)
                .load(imageResId)
                .into(imgTrainer)
        } else {
            Glide.with(this)
                .load(R.drawable.male)
                .into(imgTrainer)
        }

        loadWorkouts(trainerId)

        findViewById<ImageButton>(R.id.btnChatHeader).setOnClickListener {
            val intentChat = Intent(this, ChatActivity::class.java)
            intentChat.putExtra("trainer_id", trainerId)
            intentChat.putExtra("trainer_name", name)
            startActivity(intentChat)
        }
    }

    private fun loadWorkouts(trainerId: Int) {
        val dbHelper = DatabaseHelper(this)
        val data = dbHelper.getWorkoutsByTrainer(trainerId)
        
        recyclerWorkout.adapter = WorkoutAdapter(data)
    }
}