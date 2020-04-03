package com.tal.android.pingpong.ui.mvp.presenter

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.activities.MainActivity
import com.tal.android.pingpong.ui.activities.SplashActivity
import com.tal.android.pingpong.ui.mvp.model.LoginModel
import com.tal.android.pingpong.ui.mvp.view.LoginView
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
            try {
                GoogleSignIn
                    .getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)?.let { user ->
                        model.login(user)
                    }
            } catch (e: ApiException) {
                view.showToast(R.string.login_error, e.message)
            }
        }
    }

    @Subscribe
    fun onUserLoggedInSuccessfully(event: LoginModel.UserLoggedInSuccessfullyEvent) {
        view.showToast(R.string.login_success)
        startActivity(SplashActivity::class.java, finishCurrent = true)
    }

    @Subscribe
    fun onUserLogInFailed(event: LoginModel.UserLogInFailedEvent) {
        view.showToast(R.string.login_error, event.message)
    }
}
