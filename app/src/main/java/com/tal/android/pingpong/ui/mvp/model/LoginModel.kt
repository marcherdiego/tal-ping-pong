package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class LoginModel(val googleSignInClient: GoogleSignInClient, private val sharedPreferences: SharedPreferencesUtils) : BaseModel() {

    private val userService = ServiceGenerator.createService(UsersService::class.java)

    fun login(googleUser: GoogleSignInAccount) {
        val user = User().apply {
            userToken = googleUser.idToken
            userName = googleUser.displayName
            userEmail = googleUser.email
            userImage = googleUser.photoUrl?.toString()
        }
        userService
            .login(user)
            .enqueueResponseNotNull(
                success = {
                    sharedPreferences.saveUser(it)
                    bus.post(UserLoggedInSuccessfullyEvent())
                },
                fail = {
                    bus.post(UserLogInFailedEvent(it.message))
                },
                model = this
            )
    }

    class UserLoggedInSuccessfullyEvent
    class UserLogInFailedEvent(val message: String?)

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
