package com.tal.android.pingpong.ui.mvp.model.championship

import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.ui.mvp.model.BaseModel
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class ChampionshipUsersListModel(private val sharedPreferencesUtils: SharedPreferencesUtils, private val championshipId: Int) : BaseModel() {

    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    var usersList = emptyList<User>()

    fun getCurrentUser() = sharedPreferencesUtils.getUser()

    fun fetchUsers() {
        championshipsService
            .getChampionshipMembers(championshipId)
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

    fun updateCurrentUser(user: User) {
        sharedPreferencesUtils.saveUser(user)
    }

    class UsersFetchedSuccessfullyEvent
    class UsersFetchFailedEvent
}
