package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MatchRecord(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("local")
    var local: User? = null,

    @SerializedName("localCompanion")
    var localCompanion: User? = null,

    @SerializedName("visitor")
    var visitor: User? = null,

    @SerializedName("visitorCompanion")
    var visitorCompanion: User? = null,

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
    var requestedLocalScore: Int = UNSET,

    @SerializedName("requestedVisitorScore")
    var requestedVisitorScore: Int = UNSET,

    @SerializedName("localCompanionUserConfirmed")
    var localCompanionUserConfirmed: Boolean? = null,

    @SerializedName("visitorUserConfirmed")
    var visitorUserConfirmed: Boolean? = null,

    @SerializedName("visitorCompanionUserConfirmed")
    var visitorCompanionUserConfirmed: Boolean? = null,

    @SerializedName("championship")
    var championship: Championship? = null
) : Serializable {

    fun myVictory(myEmail: String?): Boolean {
        return if (local?.userEmail == myEmail || localCompanion?.userEmail == myEmail) {
            // I'm in the local team
            localScore > visitorScore
        } else {
            visitorScore > localScore
        }
    }

    fun hasTempRequestChanges(localScore: Int, visitorScore: Int): Boolean {
        return if (requestedLocalScore == UNSET && requestedVisitorScore == UNSET) {
            localScore != this.localScore || visitorScore != this.visitorScore
        } else {
            localScore != requestedLocalScore || visitorScore != requestedVisitorScore
        }
    }

    fun isSinglesMatch() = localCompanion == null && visitorCompanion == null

    fun userConfirmedDoublesMatch(userId: Int?): Boolean {
        if (local?.userId == userId) {
            return true
        }
        if (localCompanion?.userId == userId) {
            return localCompanionUserConfirmed == true
        }
        if (visitor?.userId == userId) {
            return visitorUserConfirmed == true
        }
        if (visitorCompanion?.userId == userId) {
            return visitorCompanionUserConfirmed == true
        }
        return false
    }

    fun isLocalUser(user: User): Boolean {
        if (local?.userId == user.userId || localCompanion?.userId == user.userId) {
            return true
        }
        return false
    }

    fun isLocalUser(userId: Int?): Boolean {
        if (local?.userId == userId || localCompanion?.userId == userId) {
            return true
        }
        return false
    }

    companion object {
        const val UNSET = -1
    }
}