package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.fireAndForget
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UserProfileModel(
    private val googleSignInClient: GoogleSignInClient,
    private val sharedPreferences: SharedPreferencesUtils,
    val user: User
) : BaseEventsModel() {

    private val usersService = ServiceGenerator.createService(UsersService::class.java)

    fun getLoggedUser() = sharedPreferences.getUser()

    fun logout() {
        val user = sharedPreferences.getUser() ?: return
        sharedPreferences.saveUser(null)
        googleSignInClient.signOut()
        usersService
            .logout(user)
            .fireAndForget()
    }

}
