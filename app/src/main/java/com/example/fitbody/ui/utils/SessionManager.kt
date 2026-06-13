package com.example.fitbody.utils

import android.content.Context

class SessionManager(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "fitbody_session",
            Context.MODE_PRIVATE
        )

    fun saveLogin(
        username: String,
        role: String,
        userId: Int
    ) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("username", username)
            .putString("role", role)
            .putInt("user_id", userId)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(
            "is_logged_in",
            false
        )
    }

    fun getUsername(): String {
        return prefs.getString(
            "username",
            ""
        ) ?: ""
    }

    fun getRole(): String {
        return prefs.getString(
            "role",
            ""
        ) ?: ""
    }

    fun getUserId(): Int {
        return prefs.getInt(
            "user_id",
            0
        )
    }

    fun logout() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun setDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean("dark_mode", isDark).apply()
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", true)
    }
}