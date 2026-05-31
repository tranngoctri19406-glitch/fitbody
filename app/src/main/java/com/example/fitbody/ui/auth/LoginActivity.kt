package com.example.fitbody.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.MainActivity
import com.example.fitbody.PtMainActivity
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.LoginResponse
import com.example.fitbody.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)

        if (session.isLoggedIn()) {
            openMain()
            return
        }

        setContentView(R.layout.activity_login)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {

        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                "Vui lòng nhập đầy đủ tài khoản và mật khẩu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        btnLogin.isEnabled = false

        RetrofitClient.instance
            .login(username, password)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    btnLogin.isEnabled = true

                    if (!response.isSuccessful || response.body() == null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Lỗi kết nối máy chủ",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val result = response.body()!!

                    if (result.success) {

                        val role = (result.role ?: "user").lowercase()
                        val userId = result.user_id ?: 0
                        val accountName = result.username ?: username

                        val session = SessionManager(this@LoginActivity)

                        session.saveLogin(
                            accountName,
                            role,
                            userId
                        )

                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công",
                            Toast.LENGTH_SHORT
                        ).show()

                        if (role == "pt") {
                            openPt(userId, accountName)
                        } else {
                            openMain(userId, accountName)
                        }

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Sai tài khoản hoặc mật khẩu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<LoginResponse>,
                    t: Throwable
                ) {
                    btnLogin.isEnabled = true

                    Toast.makeText(
                        this@LoginActivity,
                        "Không kết nối được server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun openMain(userId: Int = 0, username: String = "") {
        val intent = Intent(
            this,
            MainActivity::class.java
        )

        intent.putExtra("user_id", userId)
        intent.putExtra("username", username)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun openPt(userId: Int, username: String) {
        val intent = Intent(
            this,
            PtMainActivity::class.java
        )

        intent.putExtra("user_id", userId)
        intent.putExtra("username", username)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}