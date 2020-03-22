package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.IntDef
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.MatchRecord

class MainModel(
    var challengeMatch: MatchRecord? = null,
    @State
    @get:State
    var currentState: Int = UNSET
) : BaseEventsModel() {

    fun acceptChallenge(match: MatchRecord) {
    }

    companion object {
        /**
         * Possible screen states
         */
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(UNSET, MATCHES, FIND_RIVAL, RANKING, PROFILE)
        annotation class State

        const val UNSET = -1
        const val MATCHES = 0
        const val FIND_RIVAL = 1
        const val RANKING = 2
        const val PROFILE = 3
    }
}
