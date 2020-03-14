package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {

    @SerializedName("id")
    var userId: Int? = null

    @SerializedName("token")
    var userToken: String? = null

    @SerializedName("name")
    var userName: String? = null

    @SerializedName("email")
    var userEmail: String? = null

    @SerializedName("image")
    var userImage: String? = null

    @SerializedName("rank")
    var userRank: Int = 0

    @SerializedName("matchesWon")
    var matchesWon: Int = 0

    @SerializedName("matchesLost")
    var matchesLost: Int = 0

    @SerializedName("active")
    var isActive: Boolean? = null

    @Transient
    var matchesRatio: String? = null
        get() = if (matchesLost == 0) {
            "---"
        } else {
            (matchesWon.toFloat() / matchesLost.toFloat()).toString()
        }

    constructor()

    constructor(userToken: String?, userName: String?, userEmail: String?, userImage: String?) {
        this.userToken = userToken
        this.userName = userName
        this.userEmail = userEmail
        this.userImage = userImage
        this.isActive = true
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