package com.tal.android.pingpong.ui.mvp.model.matcheslist

import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class ReadOnlyMatchesModel(sharedPreferences: SharedPreferencesUtils) : BaseMatchesListModel(sharedPreferences) {

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
}
