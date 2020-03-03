package com.tal.android.pingpong.ui.mvp.model

import android.util.Log
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.data.UsersManager
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidChallengeTimeException
import java.util.*

class UsersListModel : BaseEventsModel() {

    fun fetchUsers() {
        bus.post(UsersFetchedSuccessfullyEvent(UsersManager.users))
    }

    @Throws(InvalidChallengeTimeException::class)
    fun challengeUser(userEmail: String, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidChallengeTimeException()
        }
        Log.e("Challenge", "$userEmail, $year, $monthOfYear, $dayOfMonth, $hourOfDay, $minute")
    }

    class UsersFetchedSuccessfullyEvent(val usersList: MutableList<User>)
}
