package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.data.UsersManager

class ProfileModel(private val googleSignInClient: GoogleSignInClient, private val user: GoogleSignInAccount?) : BaseEventsModel() {
    fun logout() {
        val currentUser = UsersManager.getUser(user?.email)
        currentUser?.isActive = false
        UsersManager.saveUser(currentUser)
        googleSignInClient.signOut()
    }

}
