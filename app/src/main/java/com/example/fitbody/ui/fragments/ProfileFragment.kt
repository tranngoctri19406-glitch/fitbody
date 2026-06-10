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
import com.example.fitbody.ui.BMICalculatorActivity
import com.example.fitbody.ui.ChangePasswordActivity
import com.example.fitbody.ui.CheckInActivity
import com.example.fitbody.ui.EditProfileActivity
import com.example.fitbody.ui.PremiumPlanActivity
import com.example.fitbody.ui.WorkoutStatsActivity
import com.example.fitbody.ui.auth.LoginActivity
import com.example.fitbody.utils.SessionManager
import com.example.fitbody.ui.ShopActivity
import com.example.fitbody.ui.UserFitnessSettingActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var btnLogout: Button

    private lateinit var txtProfileName: TextView
    private lateinit var txtProfileRole: TextView

    private lateinit var txtProgress: TextView
    private lateinit var txtWorkoutStats: TextView
    private lateinit var txtSchedule: TextView
    private lateinit var txtCheckIn: TextView
    private lateinit var txtCheckInHistory: TextView
    private lateinit var txtBMI: TextView
    private lateinit var txtPremium: TextView
    private lateinit var txtEditProfile: TextView
    private lateinit var txtChangePassword: TextView
    private lateinit var txtSettings: TextView

    private lateinit var txtShop: TextView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout = view.findViewById(R.id.btnLogout)

        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileRole = view.findViewById(R.id.txtProfileRole)

        txtProgress = view.findViewById(R.id.txtProgress)
        txtWorkoutStats = view.findViewById(R.id.txtWorkoutStats)
        txtSchedule = view.findViewById(R.id.txtSchedule)
        txtCheckIn = view.findViewById(R.id.txtCheckIn)
        txtCheckInHistory = view.findViewById(R.id.txtCheckInHistory)
        txtBMI = view.findViewById(R.id.txtBMI)
        txtPremium = view.findViewById(R.id.txtPremium)
        txtEditProfile = view.findViewById(R.id.txtEditProfile)
        txtChangePassword = view.findViewById(R.id.txtChangePassword)
        txtSettings = view.findViewById(R.id.txtSettings)
        txtShop = view.findViewById(R.id.txtShop)

        loadProfileInfo()

        txtProgress.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frameLayout,
                    ProgressFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        txtWorkoutStats.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    WorkoutStatsActivity::class.java
                )
            )
        }

        txtSchedule.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frameLayout,
                    ScheduleFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        txtCheckIn.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    CheckInActivity::class.java
                )
            )
        }

        txtCheckInHistory.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frameLayout,
                    CheckInHistoryFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        txtBMI.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    BMICalculatorActivity::class.java
                )
            )
        }

        txtPremium.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    PremiumPlanActivity::class.java
                )
            )
        }

        txtEditProfile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    EditProfileActivity::class.java
                )
            )
        }

        txtChangePassword.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ChangePasswordActivity::class.java
                )
            )
        }

        txtSettings.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    UserFitnessSettingActivity::class.java
                )
            )
        }

        txtShop.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ShopActivity::class.java
                )
            )
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