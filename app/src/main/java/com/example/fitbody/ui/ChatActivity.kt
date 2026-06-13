package com.example.fitbody.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.model.Message
import com.example.fitbody.ui.adapter.ChatAdapter
import com.example.fitbody.utils.SessionManager

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerChat: RecyclerView
    private lateinit var edtMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var btnBack: TextView
    private lateinit var txtPtName: TextView

    private var trainerId = 0
    private var trainerName = ""
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        btnBack      = findViewById(R.id.btnBack)
        txtPtName    = findViewById(R.id.txtPtName)
        recyclerChat = findViewById(R.id.recyclerChat)
        edtMessage   = findViewById(R.id.edtMessage)
        btnSend      = findViewById(R.id.btnSend)

        trainerId    = intent.getIntExtra("trainer_id", 0)
        trainerName  = intent.getStringExtra("trainer_name") ?: "PT"
        userId       = SessionManager(this).getUserId()

        txtPtName.text = trainerName

        recyclerChat.layoutManager = LinearLayoutManager(this).also {
            it.stackFromEnd = true
        }

        btnBack.setOnClickListener { finish() }
        btnSend.setOnClickListener { sendMessage() }

        loadMessages()
    }

    private fun loadMessages() {
        val db   = DatabaseHelper(this)
        val list = db.getMessages(userId, trainerId)
        recyclerChat.adapter = ChatAdapter(list, userId)
        if (list.isNotEmpty())
            recyclerChat.scrollToPosition(list.size - 1)
    }

    private fun sendMessage() {
        val content = edtMessage.text.toString().trim()
        if (content.isEmpty()) {
            Toast.makeText(this, "Nhập tin nhắn", Toast.LENGTH_SHORT).show()
            return
        }
        val db     = DatabaseHelper(this)
        val result = db.sendMessage(userId, trainerId, content)
        if (result != -1L) {
            edtMessage.text.clear()
            loadMessages()
        }
    }
}