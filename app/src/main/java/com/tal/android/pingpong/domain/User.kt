package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import com.tal.android.pingpong.utils.asPercentString
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
    var active: Boolean? = null

    @SerializedName("pushToken")
    var pushToken: String? = null

    @Transient
    var matchesRatioValue: Float = 0f
        get() = if (matchesLost == 0) {
            0f
        } else {
            matchesWon.toFloat() / (matchesWon + matchesLost).toFloat()
        }

    @Transient
    var matchesRatio: String? = null
        get() = if (matchesLost == 0) {
            "---"
        } else {
            val ratio = (100 * matchesWon.toFloat()) / (matchesWon + matchesLost).toFloat()
            ratio.asPercentString()
        }

    fun chancesToWin(user: User): Float {
        val ratioSum = this.matchesRatioValue + user.matchesRatioValue
        return this.matchesRatioValue / ratioSum
    }

    fun firstName() = userName?.substringBefore(SPACE)

    companion object {
        private const val SPACE = " "
    }
}