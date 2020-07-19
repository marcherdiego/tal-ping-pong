package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import java.util.*

abstract class BaseChampionshipMatchDialog(private val users: List<User>, val myUser: User, val bus: Bus) : BaseDialog() {
    protected var userSelectorDialog: UserSelectorDialog? = null
    protected val selectedUsers = mutableListOf(myUser)

    abstract fun show(context: Context)

    protected fun getEligibleUsers(): MutableList<User> {
        val eligibleUsers = mutableListOf<User>()
        eligibleUsers.addAll(
            users.filterNot { user ->
                user.userId in selectedUsers.map { selectedUser ->
                    selectedUser.userId
                }
            }
        )
        return eligibleUsers
    }

    fun refreshUsersList() {
        userSelectorDialog?.refreshUsersList(getEligibleUsers())
    }

    protected fun openDateSelectionDialog(context: Context, match: MatchRecord) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                openTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
            },
            today[Calendar.YEAR],
            today[Calendar.MONTH],
            today[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun openTimeSelectionDialog(context: Context, match: MatchRecord, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                match.matchDate = Calendar
                    .getInstance()
                    .apply {
                        set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    }
                    .time
                    .toString()
                bus.post(CreateNewChampionshipMatchButtonClickedEvent(match))
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    class CreateNewChampionshipMatchButtonClickedEvent(val match: MatchRecord)
}