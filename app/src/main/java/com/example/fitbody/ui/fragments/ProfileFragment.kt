package com.example.fitbody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitbody.R
import com.example.fitbody.ui.auth.LoginActivity
import com.example.fitbody.utils.SessionManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var btnLogout: Button

    private lateinit var txtProgress: TextView
    private lateinit var txtEditProfile: TextView
    private lateinit var txtChangePassword: TextView
    private lateinit var txtSettings: TextView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout =
            view.findViewById(R.id.btnLogout)

        txtProgress =
            view.findViewById(R.id.txtProgress)

        txtEditProfile =
            view.findViewById(R.id.txtEditProfile)

        txtChangePassword =
            view.findViewById(R.id.txtChangePassword)

        txtSettings =
            view.findViewById(R.id.txtSettings)

        txtProgress.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frameLayout,
                    ProgressFragment()
                )
                .commit()
        }

        txtEditProfile.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tính năng đổi thông tin cá nhân sẽ làm tiếp",
                Toast.LENGTH_SHORT
            ).show()
        }

        txtChangePassword.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tính năng đổi mật khẩu sẽ làm tiếp",
                Toast.LENGTH_SHORT
            ).show()
        }

        txtSettings.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Tính năng cài đặt sẽ làm tiếp",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnLogout.setOnClickListener {
            logout()
        }
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