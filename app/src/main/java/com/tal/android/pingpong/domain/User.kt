package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import com.tal.android.pingpong.utils.asPercentString
import java.io.Serializable

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
    var pushToken: String? = null,

    @SerializedName("champion")
    var champion: Boolean? = null
) : Serializable {

    val matchesRatioValue: Float
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