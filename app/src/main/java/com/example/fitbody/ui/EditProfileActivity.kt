package com.example.fitbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: TextView
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var btnSaveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        btnBack = findViewById(R.id.btnBack)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        loadProfile()

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val sharedPreferences =
            getSharedPreferences("profile_data", MODE_PRIVATE)

        edtName.setText(
            sharedPreferences.getString("name", "Trần An")
        )

        edtEmail.setText(
            sharedPreferences.getString("email", "tranbaoan1805@gmail.com")
        )

        edtPhone.setText(
            sharedPreferences.getString("phone", "")
        )
    }

    private fun saveProfile() {
        val name = edtName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val phone = edtPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(
                this,
                "Vui lòng nhập họ tên và email",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val sharedPreferences =
            getSharedPreferences("profile_data", MODE_PRIVATE)

        sharedPreferences.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("phone", phone)
            .apply()

        Toast.makeText(
            this,
            "Cập nhật thông tin thành công",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}