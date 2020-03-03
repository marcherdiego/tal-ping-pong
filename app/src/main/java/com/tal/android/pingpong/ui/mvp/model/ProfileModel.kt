package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class ProfileModel(
    private val googleSignInClient: GoogleSignInClient,
    private val sharedPreferences: SharedPreferencesUtils
) : BaseEventsModel() {

    fun logout() {
        sharedPreferences.saveUserToken(null)
        googleSignInClient.signOut()
    }

}
