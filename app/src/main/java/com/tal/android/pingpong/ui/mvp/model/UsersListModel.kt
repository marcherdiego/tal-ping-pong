package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.data.ChallengesManager
import com.tal.android.pingpong.data.UsersManager
import com.tal.android.pingpong.domain.Challenge
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidChallengeTimeException
import java.util.*

class UsersListModel(private val loggedUserEmail: String?) : BaseEventsModel() {

    fun fetchUsers() {
        val filteredUsers = UsersManager.users.filter {
            it.userEmail != loggedUserEmail
        }
        bus.post(UsersFetchedSuccessfullyEvent(filteredUsers))
    }

    @Throws(InvalidChallengeTimeException::class)
    fun challengeUser(userEmail: String, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidChallengeTimeException()
        }
        val challenge = Challenge().apply {
            challengerEmail = loggedUserEmail
            challengedEmail = userEmail
            matchDate = selectedDateTime.time
        }
        ChallengesManager.save(challenge)
    }

    class UsersFetchedSuccessfullyEvent(val usersList: List<User>)
}
