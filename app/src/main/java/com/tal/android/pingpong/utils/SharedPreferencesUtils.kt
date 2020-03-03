package com.tal.android.pingpong.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesUtils(context: Context) {

    private var preferences: SharedPreferences? = null

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveUserToken(userToken: String?) {
        preferences
            ?.edit()
            ?.putString(USER_TOKEN, userToken)
            ?.apply()
    }

    fun getUserToken() = preferences?.getString(USER_TOKEN, null)

    companion object {
        private const val USER_TOKEN = "user_token"
    }
}
