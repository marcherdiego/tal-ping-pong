package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import com.tal.android.pingpong.utils.asPercentString
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

data class User(
    @SerializedName("id")
    var userId: Int? = null,

    @SerializedName("token")
    var userToken: String? = null,

    @SerializedName("name")
    var userName: String? = null,

    @SerializedName("email")
    var userEmail: String? = null,

    @SerializedName("image")
    var userImage: String? = null,

    @SerializedName("rank")
    var userRank: Int = 0,

    @SerializedName("matchesWon")
    var matchesWon: Int = 0,

    @SerializedName("matchesLost")
    var matchesLost: Int = 0,

    @SerializedName("active")
    var active: Boolean? = null,

    @SerializedName("pushToken")
    var pushToken: String? = null
) : Serializable {

    private val matchesRatioValue: Float
        get() = if (matchesWon + matchesLost == 0) {
            0f
        } else {
            matchesWon.toFloat() / (matchesWon + matchesLost).toFloat()
        }

    val matchesRatio: String?
        get() = if (matchesWon + matchesLost == 0) {
            "---"
        } else {
            val ratio = (100 * matchesWon.toFloat()) / (matchesWon + matchesLost).toFloat()
            ratio.asPercentString()
        }

    fun chancesToWin(user: User): Float {
        if (this.matchesRatioValue * user.matchesRatioValue == 0f) {
            // If any of the two is zero, then there is not enough data to process
            return UNKNOWN
        }
        val ratioSum = this.matchesRatioValue + user.matchesRatioValue
        val ratio = user.matchesRatioValue / ratioSum

        // Limit value to CHANCES_LOWER_BOUND% and CHANCES_UPPER_BOUND%
        return max(CHANCES_LOWER_BOUND, min(ratio, CHANCES_UPPER_BOUND))
    }

    fun firstName() = userName?.substringBefore(SPACE)

    companion object {
        private const val SPACE = " "

        const val UNKNOWN = -1f
        const val CHANCES_HALF = 0.5f

        // Limit value to 5% and 95%
        const val CHANCES_LOWER_BOUND = 0.05f
        const val CHANCES_UPPER_BOUND = 0.95f
    }
}