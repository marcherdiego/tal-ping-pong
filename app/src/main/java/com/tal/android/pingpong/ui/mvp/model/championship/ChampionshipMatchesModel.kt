package com.tal.android.pingpong.ui.mvp.model.championship

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class ChampionshipMatchesModel(sharedPreferences: SharedPreferencesUtils, private val championshipId: Int) : BaseMatchesListModel(sharedPreferences) {

    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    override fun fetchMatches() {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchesFetchFailedEvent())
            return
        }
        championshipsService
            .getChampionshipMatches(championshipId)
            .enqueueResponseNotNull(
                success = {
                    bus.post(
                        MatchesFetchedSuccessfullyEvent(
                            it
                        )
                    )
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

    class MatchEditedSuccessfullyEvent
    class MatchEditFailedEvent

    class MatchEditDeclinedSuccessfullyEvent
    class MatchEditDeclineFailedEvent
}
