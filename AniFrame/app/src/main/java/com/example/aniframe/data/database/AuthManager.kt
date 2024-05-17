package com.example.aniframe.data.database

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    fun isUserAuthenticated(): Boolean {
        return sharedPreferences.contains("auth_token")
    }
    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
    fun clearAuthToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }
    fun userExists(username: String): Boolean {
        return sharedPreferences.contains("user_$username")
    }
    fun saveUser(username: String, email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_$username", "$email|$password")
        editor.apply()
    }
    fun validateUser(username: String, password: String): Boolean {
        val userData = sharedPreferences.getString("user_$username", null) ?: return false
        val storedPassword = userData.split("|")[1]
        return password == storedPassword
    }
    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
}