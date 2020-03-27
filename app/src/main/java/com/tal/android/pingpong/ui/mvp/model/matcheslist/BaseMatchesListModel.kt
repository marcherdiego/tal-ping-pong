package com.tal.android.pingpong.ui.mvp.model.matcheslist

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

abstract class BaseMatchesListModel(private val sharedPreferences: SharedPreferencesUtils) : BaseEventsModel() {

    protected val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun getUserId() = sharedPreferences.getUser()?.userId

    fun getUserEmail() = sharedPreferences.getUser()?.userEmail

    abstract fun fetchUserMatches()

    class MatchesFetchedSuccessfullyEvent(val matches: List<Match>)
    class MatchesFetchFailedEvent
}