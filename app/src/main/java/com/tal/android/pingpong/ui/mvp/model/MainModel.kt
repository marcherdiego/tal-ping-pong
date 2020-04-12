package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.IntDef
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.events.MatchesUpdatedEvent
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MainModel(
    private val sharedPreferences: SharedPreferencesUtils,
    var challengeMatch: MatchRecord? = null,
    @State
    @get:State
    var currentState: Int = UNSET
) : BaseModel() {

    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun acceptChallenge(match: MatchRecord) {
        matchesService
            .acceptChallenge(match)
            .enqueue(
                success = {
                    bus.post(ChallengeAcceptedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeAcceptFailedEvent())
                },
                model = this
            )
    }

    fun declineChallenge(match: MatchRecord, declineReason: String) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(ChallengeDeclineFailedEvent())
            return
        }
        matchesService
            .declineChallenge(userId, match, declineReason)
            .enqueue(
                success = {
                    bus.post(ChallengeDeclinedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeDeclineFailedEvent())
                },
                model = this
            )
    }

    fun notifyUpdateLists() {
        bus.post(MatchesUpdatedEvent())
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
