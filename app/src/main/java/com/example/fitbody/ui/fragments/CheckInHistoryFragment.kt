package com.example.fitbody.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.ui.adapter.CheckInAdapter

class CheckInHistoryFragment : Fragment(R.layout.fragment_checkin_history) {

    private lateinit var btnBack: TextView
    private lateinit var recyclerCheckIn: RecyclerView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = view.findViewById(R.id.btnBack)
        recyclerCheckIn = view.findViewById(R.id.recyclerCheckIn)

        val checkInList = getCheckInHistory()

        recyclerCheckIn.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerCheckIn.adapter =
            CheckInAdapter(checkInList)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getCheckInHistory(): List<String> {
        val sharedPreferences =
            requireContext().getSharedPreferences(
                "checkin_data",
                Context.MODE_PRIVATE
            )

        val history =
            sharedPreferences.getString("checkin_history", "")

        return if (history.isNullOrEmpty()) {
            listOf("Chưa có lịch sử check-in")
        } else {
            history.split("|")
        }
    }
}