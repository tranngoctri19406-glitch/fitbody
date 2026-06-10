package com.example.fitbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Message
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.ui.adapter.MessageAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PTChatActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var txtChatTitle: TextView
    private lateinit var recyclerMessages: RecyclerView
    private lateinit var edtMessage: EditText
    private lateinit var btnSend: Button

    private var currentUserId = 1
    private var otherUserId = 2
    private var chatName = "PT"

    private val messageList = ArrayList<Message>()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_chat)

        val memberId =
            intent.getIntExtra("member_id", -1)

        val ptId =
            intent.getIntExtra("pt_id", 2)

        val ptName =
            intent.getStringExtra("pt_name") ?: "PT"

        val memberName =
            intent.getStringExtra("member_name") ?: "Member"

        if (memberId != -1) {
            currentUserId = ptId
            otherUserId = memberId
            chatName = memberName
        } else {
            currentUserId = 1
            otherUserId = ptId
            chatName = ptName
        }

        btnBack = findViewById(R.id.btnBack)
        txtChatTitle = findViewById(R.id.txtChatTitle)
        recyclerMessages = findViewById(R.id.recyclerMessages)
        edtMessage = findViewById(R.id.edtMessage)
        btnSend = findViewById(R.id.btnSend)

        txtChatTitle.text = "Chat với $chatName"

        adapter =
            MessageAdapter(
                messageList,
                currentUserId
            )

        recyclerMessages.layoutManager =
            LinearLayoutManager(this)

        recyclerMessages.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {
            sendMessage()
        }

        loadMessages()
    }

    private fun loadMessages() {
        RetrofitClient.instance.getMessages(
            currentUserId,
            otherUserId
        ).enqueue(object : Callback<List<Message>> {

            override fun onResponse(
                call: Call<List<Message>>,
                response: Response<List<Message>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    messageList.clear()
                    messageList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()

                    if (messageList.isNotEmpty()) {
                        recyclerMessages.scrollToPosition(
                            messageList.size - 1
                        )
                    }
                }
            }

            override fun onFailure(
                call: Call<List<Message>>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@PTChatActivity,
                    "Lỗi tải tin nhắn: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendMessage() {
        val message =
            edtMessage.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(
                this,
                "Vui lòng nhập tin nhắn",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        RetrofitClient.instance.sendMessage(
            currentUserId,
            otherUserId,
            message
        ).enqueue(object : Callback<SimpleResponse> {

            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val result =
                        response.body()!!

                    if (result.success) {
                        edtMessage.setText("")
                        loadMessages()
                    } else {
                        Toast.makeText(
                            this@PTChatActivity,
                            result.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@PTChatActivity,
                        "Lỗi phản hồi từ server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<SimpleResponse>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@PTChatActivity,
                    "Lỗi gửi tin nhắn: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}