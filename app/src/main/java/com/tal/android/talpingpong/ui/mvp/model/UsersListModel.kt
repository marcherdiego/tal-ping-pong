package com.tal.android.talpingpong.ui.mvp.model

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.talpingpong.domain.User
import com.tal.android.talpingpong.exceptions.InvalidChallengeTimeException
import java.util.*

class UsersListModel(context: Context) : BaseEventsModel() {
    private val currentUser = GoogleSignIn.getLastSignedInAccount(context)

    fun fetchUsers() {
        bus.post(UsersFetchedSuccessfullyEvent(mutableListOf<User>().apply {
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
            add(User(currentUser ?: return))
        }))
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
