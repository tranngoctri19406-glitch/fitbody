package com.example.fitbody.ui.pt

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R

class AddWorkoutActivity : AppCompatActivity() {

    private lateinit var editWorkoutName: EditText
    private lateinit var editSets: EditText
    private lateinit var editReps: EditText
    private lateinit var editMuscleGroup: EditText
    private lateinit var btnSaveWorkout: Button
    private lateinit var btnBack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_workout)

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
        val name = editWorkoutName.text.toString().trim()
        val sets = editSets.text.toString().trim()
        val reps = editReps.text.toString().trim()
        val muscle = editMuscleGroup.text.toString().trim()

        if (
            name.isEmpty() ||
            sets.isEmpty() ||
            reps.isEmpty() ||
            muscle.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
            this,
            "Đã lưu bài tập: $name",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}