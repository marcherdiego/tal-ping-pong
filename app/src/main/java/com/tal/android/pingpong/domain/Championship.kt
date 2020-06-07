package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Championship(
    @SerializedName("championshipId")
    var championshipId: Int? = null,

    @SerializedName("championshipDate")
    var championshipDate: String? = null,

    @SerializedName("championshipName")
    var championshipName: String? = null,

    @SerializedName("championshipImage")
    var championshipImage: String? = null,

    @SerializedName("creator")
    var creator: User? = null,

    @SerializedName("users")
    var users: List<User> = arrayListOf(),

    @SerializedName("usersCount")
    var usersCount: Int? = null
) : Serializable
