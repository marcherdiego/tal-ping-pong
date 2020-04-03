package com.tal.android.pingpong.ui.mvp.model.matcheslist

import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class UnconfirmedMatchesModel(sharedPreferences: SharedPreferencesUtils) : BaseMatchesListModel(sharedPreferences) {

    override fun fetchUserMatches() {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchesFetchFailedEvent())
            return
        }
        matchesService
            .getMyMatches(userId = userId, startDate = Date().time, endDate = null, confirmed = false, fetchMatchHistory = true)
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
}
