package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.networking.services.UsersService
import java.util.*

class RankingModel(private val sharedPreferencesUtils: SharedPreferencesUtils) : BaseEventsModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun fetchUsers() {
        userService
            .getRanking()
            .enqueueResponseNotNull(
                success = {
                    bus.post(UsersFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(UsersFetchFailedEvent())
                }
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
        val challenge = Match().apply {
            this.match = MatchRecord().apply {
                local = sharedPreferencesUtils.getUser()
                visitor = user
                matchDate = selectedDateTime.time.toString()
            }
        }
        matchesService
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
