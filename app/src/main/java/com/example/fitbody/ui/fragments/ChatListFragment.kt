package com.example.fitbody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.ui.ChatActivity
import com.example.fitbody.ui.adapter.ChatListAdapter
import com.example.fitbody.utils.SessionManager

class ChatListFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_chat_list,
            container,
            false
        )

        recycler = view.findViewById(R.id.recyclerChatList)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        dbHelper = DatabaseHelper(requireContext())
        userId = SessionManager(requireContext()).getUserId()

        loadChatList()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadChatList()
    }

    private fun loadChatList() {
        val trainers = dbHelper.getRecentChatPartners(userId)

        recycler.adapter = ChatListAdapter(trainers) { trainer ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("trainer_id", trainer.id)
            intent.putExtra("trainer_name", trainer.name)
            startActivity(intent)
        }
    }
}
