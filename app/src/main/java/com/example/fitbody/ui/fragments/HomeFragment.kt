package com.example.fitbody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.adapter.TrainerAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.model.Trainer
import com.example.fitbody.ui.BMICalculatorActivity
import com.example.fitbody.ui.CheckInActivity
import com.example.fitbody.ui.PremiumPlanActivity
import com.example.fitbody.ui.ShopActivity
import com.example.fitbody.ui.WorkoutStatsActivity
import com.example.fitbody.ui.detail.TrainerDetailActivity
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerTrainer: RecyclerView
    private lateinit var trainerAdapter: TrainerAdapter
    private val trainerList = ArrayList<Trainer>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        recyclerTrainer = view.findViewById(R.id.recyclerTrainer)

        setupTrainerRecycler()
        setupHomeMenu(view)
        loadTrainers()

        return view
    }

    private fun setupTrainerRecycler() {
        trainerAdapter = TrainerAdapter(
            trainerList,
            { trainer ->
                val intent = Intent(
                    requireContext(),
                    TrainerDetailActivity::class.java
                )

                intent.putExtra("trainer_id", trainer.id)
                intent.putExtra("trainer_name", trainer.name)
                intent.putExtra("trainer_specialty", trainer.specialty)
                intent.putExtra("trainer_image", trainer.image)
                intent.putExtra("trainer_calories", trainer.calories)
                intent.putExtra("trainer_muscle", trainer.muscle)
                intent.putExtra("trainer_schedule", trainer.schedule)
                intent.putExtra("trainer_description", trainer.description)

                startActivity(intent)
            },
            { trainer ->
                addFavorite(trainer.id)
            }
        )

        recyclerTrainer.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrainer.adapter = trainerAdapter
        recyclerTrainer.isNestedScrollingEnabled = false
    }

    private fun setupHomeMenu(view: View) {
        view.findViewById<LinearLayout>(R.id.btnBMIHome).setOnClickListener {
            startActivity(Intent(requireContext(), BMICalculatorActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.btnStatsHome).setOnClickListener {
            startActivity(Intent(requireContext(), WorkoutStatsActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.btnProgressHome).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, ProgressFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<LinearLayout>(R.id.btnCheckInHome).setOnClickListener {
            startActivity(Intent(requireContext(), CheckInActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.btnHistoryHome).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, CheckInHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<LinearLayout>(R.id.btnScheduleHome).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, ScheduleFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<LinearLayout>(R.id.btnPremiumHome).setOnClickListener {
            startActivity(Intent(requireContext(), PremiumPlanActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.btnShopHome).setOnClickListener {
            startActivity(Intent(requireContext(), ShopActivity::class.java))
        }
    }

    private fun loadTrainers() {
        RetrofitClient.instance.getTrainers()
            .enqueue(object : Callback<List<Trainer>> {

                override fun onResponse(
                    call: Call<List<Trainer>>,
                    response: Response<List<Trainer>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: emptyList()

                        trainerList.clear()
                        trainerList.addAll(data)

                        trainerAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Không tải được danh sách PT",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<Trainer>>,
                    t: Throwable
                ) {
                    Log.e("API_ERROR", t.message.toString())

                    Toast.makeText(
                        requireContext(),
                        "Lỗi kết nối server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addFavorite(trainerId: Int) {
        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        if (userId == 0) {
            Toast.makeText(
                requireContext(),
                "Bạn cần đăng nhập lại",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        RetrofitClient.instance.addFavorite(userId, trainerId)
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã thêm PT vào yêu thích ❤️",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(
                    call: Call<SimpleResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi kết nối server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}