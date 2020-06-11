package com.tal.android.pingpong.ui.mvp.model.championship

import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class ReadOnlyMatchesModel(sharedPreferences: SharedPreferencesUtils, private val championshipId: Int) : BaseMatchesListModel(sharedPreferences) {

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
}
