package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MatchRecord(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("local")
    var local: User? = null,

    @SerializedName("visitor")
    var visitor: User? = null,

    @SerializedName("date")
    var matchDate: String? = null,

    @SerializedName("localScore")
    var localScore: Int = 0,

    @SerializedName("visitorScore")
    var visitorScore: Int = 0,

    @SerializedName("confirmed")
    var confirmed: Boolean? = null,

    @SerializedName("hasRequestedChanges")
    var hasRequestedChanges: Boolean? = null,

    @SerializedName("changeRequestUserId")
    var changeRequestUserId: Int = 0,

    @SerializedName("requestedLocalScore")
    var requestedLocalScore: Int = 0,

    @SerializedName("requestedVisitorScore")
    var requestedVisitorScore: Int = 0
) : Serializable {

    fun myVictory(myEmail: String?): Boolean {
        return if (local?.userEmail == myEmail) {
            localScore > visitorScore
        } else {
            visitorScore > localScore
        }
    }
}