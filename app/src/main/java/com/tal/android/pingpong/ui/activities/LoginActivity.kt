package com.tal.android.pingpong.ui.activities

import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.LoginModel
import com.tal.android.pingpong.ui.mvp.presenter.LoginPresenter
import com.tal.android.pingpong.ui.mvp.view.LoginView

class LoginActivity : BaseActivity<LoginPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


        val googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )

        presenter = LoginPresenter(
            LoginView(this),
            LoginModel(googleSignInClient)
        )
    }
}
