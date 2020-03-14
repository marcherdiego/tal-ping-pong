package com.tal.android.pingpong.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tal.android.pingpong.domain.User

class SharedPreferencesUtils(context: Context) {

    private var preferences: SharedPreferences? = null

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getUser(): User? {
        val userJson = preferences?.getString(USER, null)
        return if (userJson == null) {
            null
        } else {
            gson.fromJson(userJson, User::class.java)
        }
    }

    fun saveUser(user: User?) {
        val userJson = if (user == null) {
            null
        } else {
            gson.toJson(user)
        }
        preferences
            ?.edit()
            ?.putString(USER, userJson)
            ?.apply()
    }

    companion object {
        private const val USER = "user"

        private val gson = Gson()
    }
}
