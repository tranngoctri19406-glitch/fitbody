package com.example.fitbody.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitbody.R
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

        loadProfileInfo()

        btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        txtDarkMode.setOnClickListener {
            Toast.makeText(requireContext(), "Chế độ tối đang phát triển", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        loadProfileInfo()
    }

    private fun loadProfileInfo() {
        val sharedPreferences =
            requireContext().getSharedPreferences(
                "profile_data",
                Context.MODE_PRIVATE
            )

        val name =
            sharedPreferences.getString(
                "name",
                "Trần An"
            )

        val email =
            sharedPreferences.getString(
                "email",
                "FitBody Member"
            )

        txtProfileName.text = name
        txtProfileRole.text = email
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