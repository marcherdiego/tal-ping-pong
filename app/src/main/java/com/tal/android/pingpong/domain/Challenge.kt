package com.tal.android.pingpong.domain

import java.util.*

class Challenge {
    var challengeKey: String? = null
    var challengerEmail: String? = null
    var challengedEmail: String? = null
    var matchDate: Date? = null

    var challengerScore: Int = 0
    var challengedScore: Int = 0
}