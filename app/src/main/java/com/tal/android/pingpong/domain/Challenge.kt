package com.tal.android.pingpong.domain

import java.util.*

class Challenge {
    var challengerUser: User? = null
    var challengedUser: User? = null
    var matchDate: Date? = null

    var challengerScore: Int = 0
    var challengedScore: Int = 0
}