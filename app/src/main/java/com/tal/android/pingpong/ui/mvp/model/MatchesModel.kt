package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MatchesModel(private val sharedPreferences: SharedPreferencesUtils) : BaseEventsModel() {

    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun fetchUserMatches() {
        val userId = sharedPreferences.getUser()?.userId
        if (userId == null) {
            bus.post(MatchesFetchFailedEvent())
            return
        }
        matchesService
            .getMyMatches(userId)
            .enqueueResponseNotNull(
                success = {
                    bus.post(MatchesFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(MatchesFetchFailedEvent())
                }
            )
    }

    fun getUserEmail() = sharedPreferences.getUser()?.userEmail

    class MatchesFetchedSuccessfullyEvent(val matches: List<Match>)
    class MatchesFetchFailedEvent
}
