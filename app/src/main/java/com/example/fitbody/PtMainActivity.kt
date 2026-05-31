package com.example.fitbody

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PtMainActivity : AppCompatActivity() {

    private var ptId: Int = 0
    private var ptName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_main)

        ptId = intent.getIntExtra("user_id", 0)
        ptName = intent.getStringExtra("username") ?: "PT"

        val txtWelcome = findViewById<TextView>(R.id.txtWelcomePt)
        val btnAddWorkout = findViewById<Button>(R.id.btnOpenAddWorkout)
        val btnChat = findViewById<Button>(R.id.btnOpenChat)

        txtWelcome.text = "Xin chào PT: $ptName"

        btnAddWorkout.setOnClickListener {
            val intent = Intent(this, AddWorkoutActivity::class.java)
            intent.putExtra("trainer_id", ptId)
            startActivity(intent)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("sender_id", ptId)
            intent.putExtra("receiver_id", 1)
            startActivity(intent)
        }
    }
}