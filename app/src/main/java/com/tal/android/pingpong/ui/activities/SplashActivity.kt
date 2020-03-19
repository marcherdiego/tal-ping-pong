package com.tal.android.pingpong.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.tal.android.pingpong.extensions.fireAndForget
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class SplashActivity : AppCompatActivity() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GoogleSignIn.getLastSignedInAccount(this)?.let {
            goToActivity(MainActivity::class.java)
            FirebaseInstanceId
                .getInstance()
                .instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val sharedPreferencesUtils = SharedPreferencesUtils(this)
                        val currentUser = sharedPreferencesUtils.getUser() ?: return@addOnCompleteListener
                        currentUser.pushToken = task.result?.token
                        userService
                            .setPushToken(currentUser)
                            .fireAndForget()
                        sharedPreferencesUtils.saveUser(currentUser)
                    }
                }
        } ?: run {
            goToActivity(LoginActivity::class.java)
        }
    }

    private fun goToActivity(activity: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activity))
        finish()
    }
}