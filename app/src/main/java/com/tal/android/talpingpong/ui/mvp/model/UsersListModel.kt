package com.tal.android.talpingpong.ui.mvp.model

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.talpingpong.domain.User

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

    class UsersFetchedSuccessfullyEvent(val usersList: MutableList<User>)
}
