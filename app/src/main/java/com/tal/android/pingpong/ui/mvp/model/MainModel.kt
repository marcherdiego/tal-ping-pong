package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.IntDef
import com.nerdscorner.mvplib.events.model.BaseEventsModel

class MainModel(
    @State
    @get:State
    var currentState: Int = CHALLENGES
) : BaseEventsModel() {

    companion object {
        /**
         * Possible screen states
         */
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(CHALLENGES, FIND_RIVAL, PROFILE)
        annotation class State

        const val CHALLENGES = 0
        const val FIND_RIVAL = 1
        const val PROFILE = 2
    }
}
