package com.example.fitbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.api.RetrofitClient
import com.example.fitbody.model.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var edtOldPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnChangePassword: Button

    private val userId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btnBack = findViewById(R.id.btnBack)
        edtOldPassword = findViewById(R.id.edtOldPassword)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        btnBack.setOnClickListener {
            finish()
        }

        btnChangePassword.setOnClickListener {
            validateAndChangePassword()
        }
    }

    private fun validateAndChangePassword() {
        val oldPassword = edtOldPassword.text.toString().trim()
        val newPassword = edtNewPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword.length < 6) {
            Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show()
            return
        }

        changePasswordApi(oldPassword, newPassword)
    }

    private fun changePasswordApi(
        oldPassword: String,
        newPassword: String
    ) {
        btnChangePassword.isEnabled = false
        btnChangePassword.text = "ĐANG XỬ LÝ..."

        RetrofitClient.instance.changePassword(
            userId,
            oldPassword,
            newPassword
        ).enqueue(object : Callback<SimpleResponse> {

            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                btnChangePassword.isEnabled = true
                btnChangePassword.text = "ĐỔI MẬT KHẨU"

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    Toast.makeText(
                        this@ChangePasswordActivity,
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (result.success) {
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Lỗi phản hồi từ server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<SimpleResponse>,
                t: Throwable
            ) {
                btnChangePassword.isEnabled = true
                btnChangePassword.text = "ĐỔI MẬT KHẨU"

                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Lỗi kết nối: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}