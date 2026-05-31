package com.example.fitbody

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWorkoutActivity : AppCompatActivity() {

    private var trainerId: Int = 0

    private lateinit var btnBack: Button
    private lateinit var editWorkoutName: EditText
    private lateinit var editSets: EditText
    private lateinit var editReps: EditText
    private lateinit var editMuscleGroup: EditText
    private lateinit var btnSaveWorkout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        trainerId = intent.getIntExtra("trainer_id", 0)

        btnBack = findViewById(R.id.btnBack)
        editWorkoutName = findViewById(R.id.editWorkoutName)
        editSets = findViewById(R.id.editSets)
        editReps = findViewById(R.id.editReps)
        editMuscleGroup = findViewById(R.id.editMuscleGroup)
        btnSaveWorkout = findViewById(R.id.btnSaveWorkout)

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveWorkout.setOnClickListener {
            saveWorkout()
        }
    }

    private fun saveWorkout() {
        val workoutName = editWorkoutName.text.toString().trim()
        val sets = editSets.text.toString().trim()
        val reps = editReps.text.toString().trim()
        val muscleGroup = editMuscleGroup.text.toString().trim()

        if (workoutName.isEmpty() || sets.isEmpty() || reps.isEmpty() || muscleGroup.isEmpty()) {
            Toast.makeText(
                this,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        RetrofitClient.instance.addWorkout(
            trainerId,
            workoutName,
            sets,
            reps,
            muscleGroup
        ).enqueue(object : Callback<SimpleResponse> {

            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                val body = response.body()

                if (response.isSuccessful && body != null && body.success) {
                    Toast.makeText(
                        this@AddWorkoutActivity,
                        "Thêm bài tập thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddWorkoutActivity,
                        "Thêm bài tập thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                Toast.makeText(
                    this@AddWorkoutActivity,
                    "Lỗi kết nối máy chủ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}