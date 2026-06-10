package com.example.fitbody.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutStatsActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtTotalWorkout: TextView
    private lateinit var txtCompletedWorkout: TextView
    private lateinit var txtPendingWorkout: TextView

    private val userId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_stats)

        btnBack = findViewById(R.id.btnBack)
        txtTotalWorkout = findViewById(R.id.txtTotalWorkout)
        txtCompletedWorkout = findViewById(R.id.txtCompletedWorkout)
        txtPendingWorkout = findViewById(R.id.txtPendingWorkout)

        btnBack.setOnClickListener {
            finish()
        }

        loadWorkoutStats()
    }

    private fun loadWorkoutStats() {
        RetrofitClient.instance.getSchedule(userId)
            .enqueue(object : Callback<List<Schedule>> {

                override fun onResponse(
                    call: Call<List<Schedule>>,
                    response: Response<List<Schedule>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val list = response.body()!!

                        val total = list.size
                        val completed = list.count {
                            it.status == "completed" || it.status == "done"
                        }
                        val pending = total - completed

                        txtTotalWorkout.text = "Tổng số buổi tập: $total"
                        txtCompletedWorkout.text = "Đã hoàn thành: $completed"
                        txtPendingWorkout.text = "Chưa hoàn thành: $pending"
                    } else {
                        Toast.makeText(
                            this@WorkoutStatsActivity,
                            "Không lấy được dữ liệu thống kê",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<Schedule>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@WorkoutStatsActivity,
                        "Lỗi kết nối: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}