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

    @SerializedName("pushToken")
    var pushToken: String? = null

    @Transient
    var matchesRatio: String? = null
        get() = if (matchesLost == 0) {
            "---"
        } else {
            val ratio = matchesWon.toFloat() / (matchesWon + matchesLost).toFloat()
            val percentage = ratio * 100
            String.format("%.2f%%", percentage)
        }
}