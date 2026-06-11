package com.example.fitbody.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Schedule
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutStatsActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtTitle: TextView
    private lateinit var txtTotalWorkout: TextView
    private lateinit var txtCompletedWorkout: TextView
    private lateinit var txtPendingWorkout: TextView
    private lateinit var txtProgressPercent: TextView
    private lateinit var txtAdvice: TextView

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_stats)

        btnBack = findViewById(R.id.btnBack)
        txtTitle = findViewById(R.id.txtTitle)
        txtTotalWorkout = findViewById(R.id.txtTotalWorkout)
        txtCompletedWorkout = findViewById(R.id.txtCompletedWorkout)
        txtPendingWorkout = findViewById(R.id.txtPendingWorkout)
        txtProgressPercent = findViewById(R.id.txtProgressPercent)
        txtAdvice = findViewById(R.id.txtAdvice)

        txtTitle.text = "Thống kê tập luyện"

        val session = SessionManager(this)
        userId = session.getUserId()

        btnBack.setOnClickListener {
            finish()
        }

        if (userId == 0) {
            Toast.makeText(
                this,
                "Bạn cần đăng nhập lại",
                Toast.LENGTH_SHORT
            ).show()
            return
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
                            it.status.equals("completed", true) ||
                                    it.status.equals("done", true) ||
                                    it.status.equals("hoàn thành", true)
                        }

                        val pending = total - completed

                        val percent = if (total > 0) {
                            (completed * 100) / total
                        } else {
                            0
                        }

                        txtTotalWorkout.text = total.toString()
                        txtCompletedWorkout.text = completed.toString()
                        txtPendingWorkout.text = pending.toString()
                        txtProgressPercent.text = "$percent%"

                        txtAdvice.text = when {
                            total == 0 -> "Bạn chưa có lịch tập nào."
                            percent >= 80 -> "Rất tốt! Bạn đang duy trì tập luyện ổn định."
                            percent >= 50 -> "Bạn đang làm khá tốt, hãy cố gắng hoàn thành thêm lịch tập."
                            else -> "Bạn nên tập đều hơn để đạt mục tiêu sức khỏe."
                        }

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