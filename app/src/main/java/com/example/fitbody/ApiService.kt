package com.example.fitbody.api

import com.example.fitbody.model.LoginResponse
import com.example.fitbody.model.Progress
import com.example.fitbody.model.SimpleResponse
import com.example.fitbody.model.Trainer
import com.example.fitbody.model.Workout
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Lấy danh sách huấn luyện viên
    @GET("get_trainers.php")
    fun getTrainers(): Call<List<Trainer>>

    // Lấy danh sách bài tập theo trainer_id
    @GET("get_workouts.php")
    fun getWorkouts(
        @Query("trainer_id") trainerId: Int
    ): Call<List<Workout>>

    // Lấy tiến trình tập luyện
    @GET("get_progress.php")
    fun getProgress(): Call<List<Progress>>

    // Đăng nhập
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Thêm huấn luyện viên vào yêu thích
    @FormUrlEncoded
    @POST("add_favorite.php")
    fun addFavorite(
        @Field("user_id") userId: Int,
        @Field("trainer_id") trainerId: Int
    ): Call<SimpleResponse>

    // Lấy danh sách yêu thích
    @GET("get_favorites.php")
    fun getFavorites(
        @Query("user_id") userId: Int
    ): Call<List<Trainer>>

    // Xóa khỏi yêu thích
    @FormUrlEncoded
    @POST("remove_favorite.php")
    fun removeFavorite(
        @Field("user_id") userId: Int,
        @Field("trainer_id") trainerId: Int
    ): Call<SimpleResponse>

    // PT thêm bài tập
    @FormUrlEncoded
    @POST("add_workout.php")
    fun addWorkout(
        @Field("trainer_id") trainerId: Int,
        @Field("workout_name") workoutName: String,
        @Field("sets_count") setsCount: String,
        @Field("reps_count") repsCount: String,
        @Field("muscle_group") muscleGroup: String
    ): Call<SimpleResponse>
}