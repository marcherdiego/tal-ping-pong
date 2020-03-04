package com.tal.android.pingpong.domain

import java.util.*

class MatchRecord {
    var local: User? = null
    var visitor: User? = null
    var matchDate: Date? = null

    var localScore: Int = 0
    var visitorScore: Int = 0

    fun myVictory(myEmail: String?): Boolean {
        return if (local?.userEmail == myEmail) {
            localScore > visitorScore
        } else {
            visitorScore > localScore
        }
    }
}