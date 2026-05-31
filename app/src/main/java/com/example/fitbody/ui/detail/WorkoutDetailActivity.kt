package com.example.fitbody.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.ui.WorkoutTimerActivity

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_workout_detail)

        btnBack =
            findViewById(R.id.btnBack)

        val txtWorkoutName =
            findViewById<TextView>(R.id.txtWorkoutName)

        val txtMuscle =
            findViewById<TextView>(R.id.txtMuscle)

        val txtSets =
            findViewById<TextView>(R.id.txtSets)

        val txtReps =
            findViewById<TextView>(R.id.txtReps)

        val txtGuide =
            findViewById<TextView>(R.id.txtGuide)

        val btnStartTimer =
            findViewById<Button>(R.id.btnStartTimer)

        val btnVideo =
            findViewById<Button>(R.id.btnVideo)

        btnBack.setOnClickListener {
            finish()
        }

        val workoutName =
            intent.getStringExtra("workout_name") ?: ""

        val sets =
            intent.getStringExtra("sets") ?: ""

        val reps =
            intent.getStringExtra("reps") ?: ""

        val muscle =
            intent.getStringExtra("muscle") ?: ""

        val videoUrl =
            intent.getStringExtra("video_url") ?: ""

        txtWorkoutName.text =
            workoutName

        txtSets.text =
            "Sets: $sets"

        txtReps.text =
            "Reps: $reps"

        txtMuscle.text =
            "Nhóm cơ: $muscle"

        txtGuide.text =
            "Giữ form đúng, siết cơ khi tập và nghỉ 60 giây giữa mỗi set."

        btnStartTimer.setOnClickListener {
            val intent =
                Intent(
                    this,
                    WorkoutTimerActivity::class.java
                )

            intent.putExtra("workout_name", workoutName)
            intent.putExtra("muscle", muscle)

            startActivity(intent)
        }

        btnVideo.setOnClickListener {
            if (videoUrl.isEmpty()) {
                Toast.makeText(
                    this,
                    "Bài tập này chưa có video hướng dẫn",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(videoUrl)
                    )
                )
            }
        }
    }
}