package com.tal.android.talpingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.nerdscorner.mvplib.events.model.BaseEventsModel

class LoginModel(val googleSignInClient: GoogleSignInClient) : BaseEventsModel() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
