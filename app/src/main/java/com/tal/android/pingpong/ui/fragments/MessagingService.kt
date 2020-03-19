package com.tal.android.pingpong.ui.fragments

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tal.android.pingpong.extensions.fireAndForget
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MessagingService : FirebaseMessagingService() {
    private val usersService = ServiceGenerator.createService(UsersService::class.java)

    override fun onNewToken(token: String) {
        val sharedPreferencesUtils = SharedPreferencesUtils(baseContext)
        val currentUser = sharedPreferencesUtils.getUser() ?: return
        currentUser.pushToken = token
        sharedPreferencesUtils.saveUser(currentUser)
        usersService
            .setPushToken(currentUser)
            .fireAndForget()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("MessagingService", "onMessageReceived")
    }
}
