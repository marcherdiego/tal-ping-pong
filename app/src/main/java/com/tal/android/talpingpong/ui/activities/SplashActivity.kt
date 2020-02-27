package com.tal.android.talpingpong.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GoogleSignIn.getLastSignedInAccount(this)?.let {
            goToActivity(MainActivity::class.java)
        } ?: run {
            goToActivity(LoginActivity::class.java)
        }
    }

    private fun goToActivity(activity: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activity))
        finish()
    }
}