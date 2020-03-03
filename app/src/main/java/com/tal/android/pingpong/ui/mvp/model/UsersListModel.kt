package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.Challenge
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidChallengeTimeException
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChallengesService
import com.tal.android.pingpong.networking.services.UsersService
import java.util.*

class UsersListModel(private val loggedUserEmail: String?) : BaseEventsModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private val challengesService = ServiceGenerator.createService(ChallengesService::class.java)

    fun fetchUsers() {
        userService
            .getUsers()
            .enqueueResponseNotNull(
                success = {
                    val filteredUsers = it.filter { user ->
                        user.userEmail != loggedUserEmail
                    }
                    bus.post(UsersFetchedSuccessfullyEvent(filteredUsers))
                },
                fail = {
                    bus.post(UsersFetchFailedEvent())
                }
            )
    }

    @Throws(InvalidChallengeTimeException::class)
    fun challengeUser(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidChallengeTimeException()
        }
        val challenge = Challenge().apply {
            challengedUser = user
            matchDate = selectedDateTime.time
        }
        challengesService
            .challengeUser(challenge)
            .enqueueResponseNotNull(
                success = {
                    bus.post(ChallengeSubmittedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChallengeSubmitFailedEvent())
                }
            )
    }

    class UsersFetchedSuccessfullyEvent(val usersList: List<User>)
    class UsersFetchFailedEvent
    class ChallengeSubmittedSuccessfullyEvent
    class ChallengeSubmitFailedEvent
}
