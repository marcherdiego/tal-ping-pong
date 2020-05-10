package com.tal.android.pingpong.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.iid.FirebaseInstanceId
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.extensions.fireAndForget
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class SplashActivity : AppCompatActivity() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private var sharedPreferencesUtils: SharedPreferencesUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtils = SharedPreferencesUtils(this)
        GoogleSignIn.getLastSignedInAccount(this)?.let {
            val currentUser = sharedPreferencesUtils?.getUser() ?: return
            goToActivity(MainActivity::class.java)
            FirebaseInstanceId
                .getInstance()
                .instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser.pushToken = task.result?.token
                        userService
                            .setPushToken(currentUser)
                            .fireAndForget()
                        sharedPreferencesUtils?.saveUser(currentUser)
                    }
                }
            fetchCurrentUser(currentUser.userId ?: return)
        } ?: run {
            goToActivity(LoginActivity::class.java)
        }
    }

    private fun fetchCurrentUser(userId: Int) {
        userService
            .getUser(userId)
            .enqueue(
                success = {
                    sharedPreferencesUtils?.saveUser(it)
                }
            )
    }

    private fun goToActivity(activity: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activity))
        finish()
    }
}