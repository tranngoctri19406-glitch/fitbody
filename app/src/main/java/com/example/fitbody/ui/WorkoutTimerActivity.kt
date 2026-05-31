package com.example.fitbody.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitbody.R
import com.example.fitbody.utils.NotificationHelper

class WorkoutTimerActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView

    private lateinit var txtTimer: TextView
    private lateinit var txtWorkoutName: TextView
    private lateinit var txtMuscle: TextView
    private lateinit var imgWorkoutGif: ImageView

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timeLeft: Long = 30000
    private var timer: CountDownTimer? = null
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_workout_timer)

        btnBack = findViewById(R.id.btnBack)

        txtTimer = findViewById(R.id.txtTimer)
        txtWorkoutName = findViewById(R.id.txtWorkoutName)
        txtMuscle = findViewById(R.id.txtMuscle)
        imgWorkoutGif = findViewById(R.id.imgWorkoutGif)

        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        btnBack.setOnClickListener {
            finish()
        }

        val workoutName =
            intent.getStringExtra("workout_name") ?: "Bài tập"

        val muscle =
            intent.getStringExtra("muscle") ?: "Nhóm cơ"

        txtWorkoutName.text =
            workoutName

        txtMuscle.text =
            "Nhóm cơ: $muscle"

        val gifRes =
            when {
                workoutName.contains("bench", true) ||
                        workoutName.contains("press", true) ||
                        muscle.contains("ngực", true) -> {
                    R.raw.bench_press
                }

                else -> {
                    R.raw.bench_press
                }
            }

        Glide.with(this)
            .asGif()
            .load(gifRes)
            .into(imgWorkoutGif)

        updateTimer()

        btnStart.setOnClickListener {
            startTimer()
        }

        btnPause.setOnClickListener {
            pauseTimer()
        }

        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        if (isRunning) return

        timer =
            object : CountDownTimer(timeLeft, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    timeLeft =
                        millisUntilFinished

                    updateTimer()
                }

                override fun onFinish() {
                    txtTimer.text =
                        "DONE 💪"

                    NotificationHelper.showNotification(
                        this@WorkoutTimerActivity
                    )

                    isRunning = false
                }

            }.start()

        isRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeft = 30000
        updateTimer()
        isRunning = false
    }

    private fun updateTimer() {
        val seconds =
            timeLeft / 1000

        val minutes =
            seconds / 60

        val remain =
            seconds % 60

        txtTimer.text =
            String.format(
                "%02d:%02d",
                minutes,
                remain
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}