package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("position")
    var position: Int? = null,

    @SerializedName("user")
    var user: User? = null,

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

    @SerializedName("goalsDifference")
    var goalsDifference: Int? = null,

    @SerializedName("points")
    var points: Int? = null
)
