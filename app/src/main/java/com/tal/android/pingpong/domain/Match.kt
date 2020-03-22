package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Match : Serializable {
    @SerializedName("match")
    var match: MatchRecord? = null

    @SerializedName("matchesHistory")
    var matchesHistory: List<MatchRecord> = arrayListOf()
}