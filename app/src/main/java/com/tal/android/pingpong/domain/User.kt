package com.tal.android.pingpong.domain

class User {

    constructor()

    constructor(userName: String?, userEmail: String?, userImage: String?) {
        this.userName = userName
        this.userEmail = userEmail
        this.userImage = userImage
        this.isActive = true
    }

    var userKey: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var userImage: String? = null
    var userRank: Int = 0

    var matchesWon: Int = 0
    var matchesLost: Int = 0

    var isActive: Boolean? = null

    @Transient
    var matchesRatio: String? = null
        get() = if (matchesLost == 0) {
            "---"
        } else {
            (matchesWon.toFloat() / matchesLost.toFloat()).toString()
        }

    fun printStats(): String {
        val matchesWon = matchesWon
        val matchesLost = matchesLost
        val matchesRatio = matchesWon.toFloat() / matchesLost.toFloat()
        return "W: $matchesWon / L: $matchesLost | Ratio: $matchesRatio"
    }

    fun printFullStats(): String {
        val matchesWon = matchesWon
        val matchesLost = matchesLost
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