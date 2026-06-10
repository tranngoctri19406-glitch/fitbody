package com.example.fitbody.ui.pt

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.ChatUser
import com.example.fitbody.ui.PTChatActivity
import com.example.fitbody.ui.adapter.ChatUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PTChatListActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var recyclerChatUsers: RecyclerView

    private val ptId = 2
    private val chatUserList = ArrayList<ChatUser>()
    private lateinit var adapter: ChatUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_chat_list)

        btnBack = findViewById(R.id.btnBack)
        recyclerChatUsers = findViewById(R.id.recyclerChatUsers)

        adapter =
            ChatUserAdapter(chatUserList) { chatUser ->
                val intent =
                    Intent(
                        this,
                        PTChatActivity::class.java
                    )

                intent.putExtra("pt_id", ptId)
                intent.putExtra("member_id", chatUser.user_id)
                intent.putExtra("member_name", chatUser.name)

                startActivity(intent)
            }

        recyclerChatUsers.layoutManager =
            LinearLayoutManager(this)

        recyclerChatUsers.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        loadChatUsers()
    }

    override fun onResume() {
        super.onResume()
        loadChatUsers()
    }

    private fun loadChatUsers() {
        RetrofitClient.instance.getChatUsers(ptId)
            .enqueue(object : Callback<List<ChatUser>> {

                override fun onResponse(
                    call: Call<List<ChatUser>>,
                    response: Response<List<ChatUser>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        chatUserList.clear()
                        chatUserList.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<List<ChatUser>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@PTChatListActivity,
                        "Lỗi tải danh sách chat: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}