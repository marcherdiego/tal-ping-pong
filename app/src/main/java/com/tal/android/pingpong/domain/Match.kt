package com.tal.android.pingpong.domain

import com.google.gson.annotations.SerializedName

class Match {
    @SerializedName("match")
    var match: MatchRecord? = null

    @SerializedName("matchesHistory")
    var matchesHistory: List<MatchRecord>? = null

    @Transient
    var label: String? = null
}