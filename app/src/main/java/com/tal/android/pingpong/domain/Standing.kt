package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Standing(
    @SerializedName("user")
    var user: User? = null,

    @SerializedName("team")
    var team: Team? = null,

    @SerializedName("played")
    var played: Int? = null,

    @SerializedName("won")
    var won: Int? = null,

    @SerializedName("lost")
    var lost: Int? = null,

    @SerializedName("goalsScored")
    var goalsScored: Int? = null,

    @SerializedName("goalsAgainst")
    var goalsAgainst: Int? = null,

    @SerializedName("points")
    var points: Int? = null
) : Serializable, Comparable<Standing> {
    fun getGoalsDifference() = (goalsScored ?: 0) - (goalsAgainst ?: 0)

    override fun compareTo(other: Standing): Int {
        return if (points == other.points) {
            getGoalsDifference().compareTo(other.getGoalsDifference())
        } else {
            points?.compareTo(other.points ?: 0) ?: 0
        }
    }
}
