package com.tal.android.talpingpong.ui.mvp.presenter

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.ui.mvp.model.LoginModel
import com.tal.android.talpingpong.ui.mvp.view.LoginView
import org.greenrobot.eventbus.Subscribe

class LoginPresenter(view: LoginView, model: LoginModel) :
    BaseActivityPresenter<LoginView, LoginModel>(view, model) {
    @Subscribe
    fun onSignInButtonClicked(event: LoginView.SignInButtonClickedEvent) {
        view.withActivity {
            startActivityForResult(
                model.googleSignInClient.signInIntent,
                LoginModel.RC_GOOGLE_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginModel.RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            completedTask.getResult(ApiException::class.java)
            view.showToast(R.string.login_success)
        } catch (e: ApiException) {
            Log.w("GSO", "signInResult:failed code=${e.statusCode}")
            view.showToast(R.string.login_error, e.message)
        }
    }
}
