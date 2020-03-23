package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.IntDef
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService

class MainModel(
    var challengeMatch: MatchRecord? = null,
    @State
    @get:State
    var currentState: Int = UNSET
) : BaseEventsModel() {

    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun acceptChallenge() {
        matchesService
            .acceptChallenge(challengeMatch ?: return)
            .enqueue(
                success = {
                    bus.post(ChallengeAcceptedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeAcceptFailedEvent())
                }
            )
    }

    fun declineChallenge() {
        matchesService
            .declineChallenge(challengeMatch ?: return)
            .enqueue(
                success = {
                    bus.post(ChallengeDeclinedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeDeclineFailedEvent())
                }
            )
    }

    class ChallengeAcceptedSuccessfullyEvent
    class ChallengeAcceptFailedEvent
    class ChallengeDeclinedSuccessfullyEvent
    class ChallengeDeclineFailedEvent

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
