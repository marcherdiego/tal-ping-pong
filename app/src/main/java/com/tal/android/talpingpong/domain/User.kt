package com.tal.android.talpingpong.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class User(googleUser: GoogleSignInAccount) {
    var userName: String? = null
    var userEmail: String? = null
    var userImage: String? = null
    var userRank: Int = 0

    var matchesWon: Int = 0
    var matchesLost: Int = 0
    var matchesRatio: String? = null
        get() = if (matchesLost == 0) {
            "---"
        } else {
            (matchesWon.toFloat() / matchesLost.toFloat()).toString()
        }

    init {
        userName = googleUser.displayName
        userEmail = googleUser.email
        userImage = googleUser.photoUrl?.toString()
    }

    fun getStats(): String {
        val matchesWon = matchesWon
        val matchesLost = matchesLost ?: 0
        val matchesRatio = matchesWon.toFloat() / matchesLost.toFloat()
        return "W: $matchesWon / L: $matchesLost | Ratio: $matchesRatio"
    }

    fun getFullStats(): String {
        val matchesWon = matchesWon ?: 0
        val matchesLost = matchesLost ?: 0
        val matchesRatio = matchesWon.toFloat() / matchesLost.toFloat()
        val matchesRatioString = if (matchesLost == 0) {
            "---"
        } else {
            matchesRatio.toString()
        }
        return "Matches won: $matchesWon\n" +
                "Matches lost: $matchesLost\n" +
                "Matches win ratio: $matchesRatioString"
    }
}