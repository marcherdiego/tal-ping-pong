package com.tal.android.pingpong.ui.mvp.model.championship

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class ChampionshipMatchesModel(
    private val sharedPreferences: SharedPreferencesUtils,
    private val championshipId: Int,
    val doubles: Boolean
) : BaseMatchesListModel(sharedPreferences) {

    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)
    val users = mutableListOf<User>()

    fun getCurrentUser() = sharedPreferences.getUser()

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

    fun updateUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
    }

    fun createMatch(match: MatchRecord) {
        val userId = getUserId()
        if (userId == null) {
            bus.post(MatchCreationFailedEvent())
            return
        }
        championshipsService
            .createChampionshipMatch(userId, championshipId, match)
            .enqueue(
                success = {
                    bus.post(MatchCreatedSuccessfullyEvent())
                },
                fail = {
                    bus.post(MatchCreationFailedEvent())
                },
                model = this
            )
    }

    class MatchEditedSuccessfullyEvent
    class MatchEditFailedEvent

    class MatchEditDeclinedSuccessfullyEvent
    class MatchEditDeclineFailedEvent

    class MatchCreatedSuccessfullyEvent
    class MatchCreationFailedEvent
}
