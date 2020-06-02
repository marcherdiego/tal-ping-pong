package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class NewChampionshipModel(private val sharedPreferencesUtils: SharedPreferencesUtils) : BaseModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private var championshipDate: Date? = null
    var usersList = emptyList<User>()
    var selectedUsers = mutableListOf<User>()

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

    @Throws(InvalidMatchTimeException::class)
    fun saveChampionshipDate(year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidMatchTimeException()
        }
        championshipDate = selectedDateTime.time
    }

    class UsersFetchedSuccessfullyEvent
    class UsersFetchFailedEvent
}
