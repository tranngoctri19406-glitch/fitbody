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
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.model.Trainer
import com.example.fitbody.ui.BMICalculatorActivity
import com.example.fitbody.ui.CheckInActivity
import com.example.fitbody.ui.PremiumPlanActivity
import com.example.fitbody.ui.ShopActivity
import com.example.fitbody.ui.WorkoutStatsActivity
import com.example.fitbody.ui.detail.TrainerDetailActivity
import com.example.fitbody.utils.SessionManager

class HomeFragment : Fragment() {

    private lateinit var recyclerTrainerTop: RecyclerView
    private lateinit var recyclerTrainerAll: RecyclerView
    
    private lateinit var trainerAdapterTop: TrainerAdapter
    private lateinit var trainerAdapterAll: TrainerAdapter
    
    private val trainerListTop = ArrayList<Trainer>()
    private val trainerListAll = ArrayList<Trainer>()

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

        recyclerTrainerTop = view.findViewById(R.id.recyclerTrainerTop)
        recyclerTrainerAll = view.findViewById(R.id.recyclerTrainerAll)

        setupTrainerRecyclers()
        setupHomeMenu(view)
        loadTrainers()

        return view
    }

    private fun setupTrainerRecyclers() {
        // Adapter cho Top Trainer
        trainerAdapterTop = createTrainerAdapter(trainerListTop)
        recyclerTrainerTop.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrainerTop.adapter = trainerAdapterTop
        recyclerTrainerTop.isNestedScrollingEnabled = false

        // Adapter cho Random Trainer
        trainerAdapterAll = createTrainerAdapter(trainerListAll)
        recyclerTrainerAll.layoutManager = LinearLayoutManager(requireContext())
        recyclerTrainerAll.adapter = trainerAdapterAll
        recyclerTrainerAll.isNestedScrollingEnabled = false
    }

    private fun createTrainerAdapter(list: ArrayList<Trainer>): TrainerAdapter {
        return TrainerAdapter(
            list,
            { trainer ->
                val intent = Intent(requireContext(), TrainerDetailActivity::class.java)
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
            },
            { trainer ->
                addLike(trainer.id)
            }
        )
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
        val dbHelper = DatabaseHelper(requireContext())
        
        // Load Top Trainers (Yêu thích nhiều nhất)
        val topData = dbHelper.getTopFavoriteTrainers()
        trainerListTop.clear()
        trainerListTop.addAll(topData)
        trainerAdapterTop.notifyDataSetChanged()

        // Load Random Trainers
        val randomData = dbHelper.getRandomTrainers()
        trainerListAll.clear()
        trainerListAll.addAll(randomData)
        trainerAdapterAll.notifyDataSetChanged()
    }

    private fun addFavorite(trainerId: Int) {
        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        if (userId == 0) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập lại", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(requireContext())
        val success = dbHelper.addFavorite(userId, trainerId)

        if (success) {
            Toast.makeText(requireContext(), "Đã thêm PT vào yêu thích ❤️", Toast.LENGTH_SHORT).show()
            // Reload lại để cập nhật số lượt yêu thích hiện lên màn hình
            loadTrainers()
        } else {
            Toast.makeText(requireContext(), "Lỗi lưu yêu thích", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addLike(trainerId: Int) {
        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        if (userId == 0) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập lại", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(requireContext())
        val success = dbHelper.addLike(userId, trainerId)

        if (success) {
            Toast.makeText(requireContext(), "Cảm ơn bạn đã thích PT này! 👍", Toast.LENGTH_SHORT).show()
            loadTrainers()
        } else {
            Toast.makeText(requireContext(), "Bạn đã thích PT này rồi hoặc có lỗi xảy ra", Toast.LENGTH_SHORT).show()
        }
    }
}