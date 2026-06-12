package com.example.fitbody.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.fitbody.R
import com.example.fitbody.database.DatabaseHelper
import com.example.fitbody.ui.ChangePasswordActivity
import com.example.fitbody.ui.EditProfileActivity
import com.example.fitbody.ui.auth.LoginActivity
import com.example.fitbody.utils.SessionManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var btnLogout: Button
    private lateinit var txtProfileName: TextView
    private lateinit var txtProfileRole: TextView
    private lateinit var btnEditProfile: Button

    private lateinit var txtDarkMode: TextView
    private lateinit var txtNotifications: TextView
    private lateinit var txtChangePassword: TextView
    private lateinit var txtTerms: TextView
    private lateinit var txtVersion: TextView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout = view.findViewById(R.id.btnLogout)
        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileRole = view.findViewById(R.id.txtProfileRole)
        btnEditProfile = view.findViewById(R.id.txtEditProfile)

        txtDarkMode = view.findViewById(R.id.txtDarkMode)
        txtNotifications = view.findViewById(R.id.txtNotifications)
        txtChangePassword = view.findViewById(R.id.txtChangePassword)
        txtTerms = view.findViewById(R.id.txtTerms)
        txtVersion = view.findViewById(R.id.txtVersion)

        updateDarkModeText()
        loadProfileInfo()

        btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        txtDarkMode.setOnClickListener {
            toggleDarkMode()
        }

        txtNotifications.setOnClickListener {
            Toast.makeText(requireContext(), "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
        }

        txtChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        txtTerms.setOnClickListener {
            Toast.makeText(requireContext(), "Điều khoản sử dụng", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun toggleDarkMode() {
        val session = SessionManager(requireContext())
        val isCurrentlyDark = session.isDarkMode()
        val newMode = !isCurrentlyDark
        
        session.setDarkMode(newMode)
        
        if (newMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Toast.makeText(requireContext(), "Đã bật chế độ tối 🌙", Toast.LENGTH_SHORT).show()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(requireContext(), "Đã bật chế độ sáng ☀️", Toast.LENGTH_SHORT).show()
        }
        updateDarkModeText()
    }

    private fun updateDarkModeText() {
        val session = SessionManager(requireContext())
        if (session.isDarkMode()) {
            txtDarkMode.text = "🌙 Chế độ tối: Bật"
        } else {
            txtDarkMode.text = "☀️ Chế độ tối: Tắt"
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileInfo()
    }

    private fun loadProfileInfo() {
        val session = SessionManager(requireContext())
        val userId = session.getUserId()

        val dbHelper = DatabaseHelper(requireContext())
        val cursor = dbHelper.getUserProfile(userId)

        if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            val email = cursor.getString(1)
            txtProfileName.text = name
            txtProfileRole.text = email
        } else {
            // Fallback
            txtProfileName.text = session.getUsername()
            txtProfileRole.text = "Thành viên FitBody"
        }
        cursor.close()
    }

    private fun logout() {
        val session =
            SessionManager(requireContext())

        session.logout()

        val intent =
            Intent(
                requireContext(),
                LoginActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}