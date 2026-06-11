package com.example.fitbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.utils.SessionManager

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
        val session = SessionManager(this)
        val userId = session.getUserId()
        val dbHelper = DatabaseHelper(this)
        val cursor = dbHelper.getUserProfile(userId)

        if (cursor.moveToFirst()) {
            edtName.setText(cursor.getString(0))
            edtEmail.setText(cursor.getString(1))
        }
        cursor.close()
    }

    private fun saveProfile() {
        val name = edtName.text.toString().trim()
        val email = edtEmail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ tên và email", Toast.LENGTH_SHORT).show()
            return
        }

        val session = SessionManager(this)
        val userId = session.getUserId()
        val dbHelper = DatabaseHelper(this)
        
        if (dbHelper.updateUserProfile(userId, name, email)) {
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}