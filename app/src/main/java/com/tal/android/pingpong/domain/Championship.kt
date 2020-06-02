package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Championship(
    @SerializedName("date")
    var championshipDate: String? = null,

    @SerializedName("name")
    var championshipName: String? = null,

    @SerializedName("users")
    var users: List<User> = arrayListOf()
) : Serializable
