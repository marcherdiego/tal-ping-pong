package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.data.UsersManager
import com.tal.android.pingpong.domain.User

class LoginModel(val googleSignInClient: GoogleSignInClient) : BaseEventsModel() {
    fun saveUser(user: User) {
        val previouslyLoggedUser = UsersManager.getUser(user.userEmail)
        if (previouslyLoggedUser != null) {
            previouslyLoggedUser.isActive = true
            UsersManager.saveUser(previouslyLoggedUser)
        } else {
            UsersManager.saveUser(user)
        }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
