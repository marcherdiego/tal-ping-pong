package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.events.ChallengeSubmitFailedEvent
import com.tal.android.pingpong.events.ChallengeSubmittedSuccessfullyEvent
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UsersListModel(private val sharedPreferencesUtils: SharedPreferencesUtils) : BaseModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    var showingFabOptions = false

    var usersList = emptyList<User>()

    fun getCurrentUser() = sharedPreferencesUtils.getUser()

    fun fetchUsers() {
        userService
            .getUsers()
            .enqueueResponseNotNull(
                success = {
                    usersList = it
                    bus.post(UsersFetchedSuccessfullyEvent())
                },
                fail = {
                    bus.post(UsersFetchFailedEvent())
                },
                model = this
            )
    }

    fun challengeUserSinglesMatch(match: MatchRecord) {
        matchesService
            .challengeUserSinglesMatch(match)
            .enqueueResponseNotNull(
                success = {
                    bus.post(ChallengeSubmittedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeSubmitFailedEvent())
                },
                model = this
            )
    }

    fun challengeUsersDoublesMatch(match: MatchRecord) {
        matchesService
            .challengeUsersDoublesMatch(match)
            .enqueueResponseNotNull(
                success = {
                    bus.post(ChallengeSubmittedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeSubmitFailedEvent())
                },
                model = this
            )
    }

    fun updateCurrentUser(user: User) {
        sharedPreferencesUtils.saveUser(user)
    }

    class UsersFetchedSuccessfullyEvent
    class UsersFetchFailedEvent
}
