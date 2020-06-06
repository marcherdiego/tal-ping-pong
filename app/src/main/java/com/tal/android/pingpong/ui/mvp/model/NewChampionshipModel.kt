package com.tal.android.pingpong.ui.mvp.model

import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidChampionshipNameException
import com.tal.android.pingpong.exceptions.InvalidChampionshipTimeException
import com.tal.android.pingpong.exceptions.InvalidChampionshipUsersListException
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.ChampionshipsService
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class NewChampionshipModel(private val sharedPreferencesUtils: SharedPreferencesUtils) : BaseModel() {
    private val userService = ServiceGenerator.createService(UsersService::class.java)
    private val championshipsService = ServiceGenerator.createService(ChampionshipsService::class.java)

    var championshipDate: Date? = null
    var championshipName: String? = null
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

    @Throws(InvalidChampionshipTimeException::class, InvalidChampionshipNameException::class, InvalidChampionshipUsersListException::class)
    fun createChampionship() {
        val championshipDate = championshipDate ?: throw InvalidChampionshipTimeException()
        val championshipName = championshipName ?: throw InvalidChampionshipNameException()
        if (selectedUsers.size < CHAMPIONSHIP_MINIMUM_USERS) {
            throw InvalidChampionshipUsersListException()
        }
        val championship = Championship(
            championshipDate = championshipDate.toString(),
            championshipName = championshipName,
            users = selectedUsers
        )
        championshipsService
            .createChampionship(championship)
            .enqueue(
                success = {
                    bus.post(ChampionshipCreatedSuccessfullyEvent())
                },
                fail = {
                    bus.post(ChampionshipCreationFailedEvent())
                },
                model = this
            )
    }

    class UsersFetchedSuccessfullyEvent
    class UsersFetchFailedEvent

    class ChampionshipCreatedSuccessfullyEvent
    class ChampionshipCreationFailedEvent

    companion object {
        private const val CHAMPIONSHIP_MINIMUM_USERS = 4
    }
}
