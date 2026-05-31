package com.example.fitbody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbody.R
import com.example.fitbody.adapter.TrainerAdapter
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.model.Trainer
import com.example.fitbody.ui.detail.TrainerDetailActivity
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var recyclerFavorite: RecyclerView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        recyclerFavorite =
            view.findViewById(R.id.recyclerFavorite)

        recyclerFavorite.layoutManager =
            LinearLayoutManager(requireContext())

        loadFavorites()
    }

    private fun loadFavorites() {

        val session =
            SessionManager(requireContext())

        val userId =
            session.getUserId()

        RetrofitClient.instance
            .getFavorites(userId)
            .enqueue(object : Callback<List<Trainer>> {

                override fun onResponse(
                    call: Call<List<Trainer>>,
                    response: Response<List<Trainer>>
                ) {
                    if (response.isSuccessful) {

                        val list =
                            response.body() ?: emptyList()

                        recyclerFavorite.adapter =
                            TrainerAdapter(

                                list,

                                { trainer ->

                                    val intent =
                                        Intent(
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

                                    removeFavorite(
                                        trainer.id
                                    )
                                }
                            )
                    }
                }

                override fun onFailure(
                    call: Call<List<Trainer>>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Không tải được danh sách yêu thích",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun removeFavorite(
        trainerId: Int
    ) {

        val session =
            SessionManager(requireContext())

        val userId =
            session.getUserId()

        RetrofitClient.instance
            .removeFavorite(
                userId,
                trainerId
            )
            .enqueue(object : Callback<SimpleResponse> {

                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Đã bỏ khỏi yêu thích",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadFavorites()
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