package com.example.fitbody.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Schedule
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.ui.adapter.ScheduleAdapter
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    private lateinit var btnBack: TextView
    private lateinit var edtDayName: EditText
    private lateinit var edtWorkoutPlan: EditText
    private lateinit var btnAddSchedule: Button
    private lateinit var recyclerSchedule: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack = view.findViewById(R.id.btnBack)
        edtDayName = view.findViewById(R.id.edtDayName)
        edtWorkoutPlan = view.findViewById(R.id.edtWorkoutPlan)
        btnAddSchedule = view.findViewById(R.id.btnAddSchedule)
        recyclerSchedule = view.findViewById(R.id.recyclerSchedule)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        recyclerSchedule.layoutManager =
            LinearLayoutManager(requireContext())

        loadSchedule()

        btnAddSchedule.setOnClickListener {
            addSchedule()
        }
    }

    private fun loadSchedule() {
        val userId =
            SessionManager(requireContext()).getUserId()

        RetrofitClient.instance
            .getSchedule(userId)
            .enqueue(object : Callback<List<Schedule>> {
                override fun onResponse(
                    call: Call<List<Schedule>>,
                    response: Response<List<Schedule>>
                ) {
                    if (response.isSuccessful) {
                        val list = response.body() ?: emptyList()

                        recyclerSchedule.adapter =
                            ScheduleAdapter(
                                list,
                                { schedule ->
                                    completeSchedule(schedule.id)
                                },
                                { schedule ->
                                    deleteSchedule(schedule.id)
                                }
                            )
                    }
                }

                override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Không tải được lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addSchedule() {
        val dayName = edtDayName.text.toString().trim()
        val workoutPlan = edtWorkoutPlan.text.toString().trim()

        if (dayName.isEmpty() || workoutPlan.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Vui lòng nhập đầy đủ lịch tập",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userId =
            SessionManager(requireContext()).getUserId()

        RetrofitClient.instance
            .addSchedule(userId, dayName, workoutPlan)
            .enqueue(object : Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã thêm lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    edtDayName.text.clear()
                    edtWorkoutPlan.text.clear()

                    loadSchedule()
                }

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi thêm lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun completeSchedule(id: Int) {
        RetrofitClient.instance
            .completeSchedule(id)
            .enqueue(object : Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã hoàn thành lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadSchedule()
                }

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi cập nhật",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun deleteSchedule(id: Int) {
        RetrofitClient.instance
            .deleteSchedule(id)
            .enqueue(object : Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã xóa lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadSchedule()
                }

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi xóa lịch tập",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}