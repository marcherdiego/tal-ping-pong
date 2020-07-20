package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Team(
    @SerializedName("id")
    var teamId: Int? = null,

    @SerializedName("user1")
    var user1: User? = null,

    @SerializedName("user2")
    var user2: User? = null
) : Serializable
