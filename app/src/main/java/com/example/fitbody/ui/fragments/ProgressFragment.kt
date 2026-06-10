package com.example.fitbody.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.Progress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressFragment : Fragment() {

    private lateinit var btnBack: TextView
    private lateinit var txtWorkoutCount: TextView
    private lateinit var txtCalories: TextView
    private lateinit var txtStreak: TextView
    private lateinit var progressWorkout: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =
            inflater.inflate(
                R.layout.fragment_progress,
                container,
                false
            )

        btnBack = view.findViewById(R.id.btnBack)
        txtWorkoutCount =
            view.findViewById(R.id.txtWorkoutCount)

        txtCalories =
            view.findViewById(R.id.txtCalories)

        txtStreak =
            view.findViewById(R.id.txtStreak)

        progressWorkout =
            view.findViewById(R.id.progressWorkout)

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        loadProgress()

        return view
    }

    private fun loadProgress() {

        RetrofitClient.instance
            .getProgress()
            .enqueue(object : Callback<List<Progress>> {

                override fun onResponse(
                    call: Call<List<Progress>>,
                    response: Response<List<Progress>>
                ) {
                    if (response.isSuccessful) {

                        val progress =
                            response.body()?.firstOrNull()

                        if (progress != null) {

                            txtWorkoutCount.text =
                                progress.workout_count.toString()

                            txtCalories.text =
                                "${progress.calories_burned} kcal"

                            txtStreak.text =
                                "${progress.streak_days} ngày 🔥"

                            progressWorkout.progress =
                                progress.progress_percent

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Chưa có dữ liệu tiến độ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Lỗi tải tiến độ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<Progress>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Không kết nối được server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}