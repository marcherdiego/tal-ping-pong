package com.tal.android.pingpong.ui.mvp.model.matcheslist

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.extensions.attachTo
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
                }
            )
            .attachTo(this)
    }

    fun editMatch(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchesFetchFailedEvent())
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
                }
            )
            .attachTo(this)
    }

    fun isMyMatchEdit(match: MatchRecord) = match.changeRequestUserId == getUserId()

    class MatchEditedSuccessfullyEvent
    class MatchEditFailedEvent
}
