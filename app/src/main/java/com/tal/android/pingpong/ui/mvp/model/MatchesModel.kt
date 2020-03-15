package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

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
                success = { matches ->
                    val allMatchesItems = mutableListOf<Match>()
                    val nowTime = Date()
                    val upcomingMatches = matches
                        .filter {
                            val matchDate = DateUtils.toDate(it.match?.matchDate)
                            matchDate?.after(nowTime) ?: false
                        }
                        .toMutableList()
                    upcomingMatches.addAll(upcomingMatches)
                    upcomingMatches.addAll(upcomingMatches)
                    upcomingMatches.addAll(upcomingMatches)
                    upcomingMatches.addAll(upcomingMatches)
                    upcomingMatches.addAll(upcomingMatches)
                    upcomingMatches.addAll(upcomingMatches)
                    val pastMatches = matches
                        .filter {
                            val matchDate = DateUtils.toDate(it.match?.matchDate)
                            matchDate?.before(nowTime) ?: false
                        }
                        .toMutableList()
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    pastMatches.addAll(pastMatches)
                    if (upcomingMatches.isNotEmpty()) {
                        allMatchesItems.add(Match().apply {
                            label = "Upcoming matches"
                        })
                        allMatchesItems.addAll(upcomingMatches)
                    }
                    if (pastMatches.isNotEmpty()) {
                        allMatchesItems.add(Match().apply {
                            label = "Past matches"
                        })
                        allMatchesItems.addAll(pastMatches)
                    }
                    bus.post(MatchesFetchedSuccessfullyEvent(allMatchesItems))
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
