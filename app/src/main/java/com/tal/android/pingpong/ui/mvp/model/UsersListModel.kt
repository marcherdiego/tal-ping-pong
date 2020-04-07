package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class UsersListModel(private val sharedPreferencesUtils: SharedPreferencesUtils) : BaseModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    var showingFabOptions = false

    fun getCurrentUser() = sharedPreferencesUtils.getUser()

    fun fetchUsers() {
        userService
            .getUsers()
            .enqueueResponseNotNull(
                success = {
                    bus.post(UsersFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(UsersFetchFailedEvent())
                },
                model = this
            )
    }

    @Throws(InvalidMatchTimeException::class)
    fun challengeUser(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidMatchTimeException()
        }
        val match = MatchRecord().apply {
            local = getCurrentUser()
            visitor = user
            matchDate = selectedDateTime.time.toString()
        }
        matchesService
            .challengeUser(match)
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

    class UsersFetchedSuccessfullyEvent(val usersList: List<User>)
    class UsersFetchFailedEvent
    class ChallengeSubmittedSuccessfullyEvent
    class ChallengeSubmitFailedEvent
}
