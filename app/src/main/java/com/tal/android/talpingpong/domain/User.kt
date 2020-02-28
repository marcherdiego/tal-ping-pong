package com.tal.android.talpingpong.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class User(googleUser: GoogleSignInAccount) {

    var userName: String? = null
    var userEmail: String? = null
    var userImage: String? = null

    var matchesWon: Int? = 0
    var matchesLost: Int? = 0

    init {
        userName = googleUser.displayName
        userEmail = googleUser.email
        userImage = googleUser.photoUrl?.toString()
    }
}