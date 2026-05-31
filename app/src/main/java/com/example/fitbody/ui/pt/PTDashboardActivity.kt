package com.example.fitbody.ui.pt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.adapter.WorkoutAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Workout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PTDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerWorkout: RecyclerView
    private lateinit var btnAddWorkout: Button
    private lateinit var btnBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pt_dashboard)

        btnBack = findViewById(R.id.btnBack)
        recyclerWorkout = findViewById(R.id.recyclerWorkout)
        btnAddWorkout = findViewById(R.id.btnAddWorkout)

        btnBack.setOnClickListener {
            finish()
        }

        recyclerWorkout.layoutManager =
            LinearLayoutManager(this)

        loadWorkout()

        btnAddWorkout.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddWorkoutActivity::class.java
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadWorkout()
    }

    private fun loadWorkout() {
        RetrofitClient.instance
            .getWorkouts(1)
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