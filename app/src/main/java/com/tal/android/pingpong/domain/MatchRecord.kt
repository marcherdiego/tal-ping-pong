package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MatchRecord : Serializable {
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("local")
    var local: User? = null

    @SerializedName("visitor")
    var visitor: User? = null

    @SerializedName("date")
    var matchDate: String? = null

    @SerializedName("localScore")
    var localScore: Int = 0

    @SerializedName("visitorScore")
    var visitorScore: Int = 0

    fun myVictory(myEmail: String?): Boolean {
        return if (local?.userEmail == myEmail) {
            localScore > visitorScore
        } else {
            visitorScore > localScore
        }
    }
}