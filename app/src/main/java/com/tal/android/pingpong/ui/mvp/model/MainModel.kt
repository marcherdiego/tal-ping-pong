package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.events.MatchesUpdatedEvent
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.notifications.Constants
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MainModel(
    private val sharedPreferences: SharedPreferencesUtils,
    var match: MatchRecord? = null,
    @ScreenState
    @get:ScreenState
    var initialScreenState: Int = UNSET,
    @MatchesListModel.Companion.TabsState
    @get:MatchesListModel.Companion.TabsState
    var matchesTabsState: String = MatchesListModel.Companion.TabsState.UPCOMING,
    @ActionType
    @get:ActionType
    var actionType: String = Constants.NONE
) : BaseModel() {

    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    @ScreenState
    @get:ScreenState
    var currentState: Int = UNSET

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun acceptSinglesChallenge(match: MatchRecord) {
        matchesService
            .acceptSinglesChallenge(match)
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

    fun acceptDoublesChallenge(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(ChallengeAcceptFailedEvent())
            return
        }
        matchesService
            .acceptDoublesChallenge(userId, match)
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
        @IntDef(UNSET, MATCHES, EVENTS, FIND_RIVAL, RANKING, PROFILE)
        annotation class ScreenState

        const val UNSET = -1
        const val MATCHES = 0
        const val EVENTS = 1
        const val FIND_RIVAL = 2
        const val RANKING = 3
        const val PROFILE = 4

        /**
         * Possible screen source
         */
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(
            Constants.NONE, Constants.INCOMING_SINGLES_CHALLENGE, Constants.CHALLENGE_INVITE_ACCEPTED, Constants.CHALLENGE_INVITE_DECLINED,
            Constants.CHALLENGE_EDIT_REQUEST, Constants.CHALLENGE_EDIT_ACCEPTED, Constants.CHALLENGE_EDIT_DECLINED
        )
        annotation class ActionType
    }
}
