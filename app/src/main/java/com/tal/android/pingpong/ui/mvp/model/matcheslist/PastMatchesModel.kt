package com.tal.android.pingpong.ui.mvp.model.matcheslist

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class PastMatchesModel(sharedPreferences: SharedPreferencesUtils) : BaseMatchesListModel(sharedPreferences) {

    override fun fetchUserMatches() {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchesFetchFailedEvent())
            return
        }
        matchesService
            .getMyMatches(userId = userId, startDate = null, endDate = Date().time)
            .enqueueResponseNotNull(
                success = {
                    bus.post(MatchesFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(MatchesFetchFailedEvent())
                },
                model = this
            )
    }

    fun editMatch(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchEditFailedEvent())
            return
        }
        matchesService
            .editMatch(userId, match)
            .enqueueResponseNotNull(
                success = {
                    bus.post(MatchEditedSuccessfullyEvent())
                },
                fail = {
                    bus.post(MatchEditFailedEvent())
                },
                model = this
            )
    }

    fun acceptMatchEdit(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchEditAcceptFailedEvent())
            return
        }
        matchesService
            .acceptMatchEdit(userId, match)
            .enqueueResponseNotNull(
                success = {
                    bus.post(MatchEditAcceptedSuccessfullyEvent())
                },
                fail = {
                    bus.post(MatchEditAcceptFailedEvent())
                },
                model = this
            )
    }

    fun isMyMatchEdit(match: MatchRecord) = match.changeRequestUserId == getUserId() || match.hasRequestedChanges == false

    fun declineMatchEdit(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchEditDeclineFailedEvent())
            return
        }
        matchesService
            .declineMatchEdit(userId, match)
            .enqueueResponseNotNull(
                success = {
                    bus.post(MatchEditDeclinedSuccessfullyEvent())
                },
                fail = {
                    bus.post(MatchEditDeclineFailedEvent())
                },
                model = this
            )

    }

    class MatchEditedSuccessfullyEvent
    class MatchEditFailedEvent

    class MatchEditAcceptedSuccessfullyEvent
    class MatchEditAcceptFailedEvent

    class MatchEditDeclinedSuccessfullyEvent
    class MatchEditDeclineFailedEvent
}
