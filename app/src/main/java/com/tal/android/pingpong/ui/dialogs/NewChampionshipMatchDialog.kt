package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load
import org.greenrobot.eventbus.Subscribe
import java.util.*

class NewChampionshipMatchDialog(private val users: List<User>, private val myUser: User, private val bus: Bus) : BaseDialog() {
    private var userSelectorDialog: UserSelectorDialog? = null

    private lateinit var localImage: ImageView
    private lateinit var localUserName: TextView

    private lateinit var visitorImage: ImageView
    private lateinit var visitorUserName: TextView

    private lateinit var localScore: EditText
    private lateinit var visitorScore: EditText

    private var visitor: User? = null

    fun show(context: Context) {
        val userSelectorBus = Bus.newInstance
        userSelectorBus.register(this)
        val newMatchDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_championship_match_dialog, null)
        localImage = newMatchDialogView.findViewById(R.id.local_image)
        localUserName = newMatchDialogView.findViewById(R.id.local_user_name)
        visitorImage = newMatchDialogView.findViewById(R.id.visitor_image)
        visitorUserName = newMatchDialogView.findViewById(R.id.visitor_user_name)

        localScore = newMatchDialogView.findViewById(R.id.local_score)
        visitorScore = newMatchDialogView.findViewById(R.id.visitor_score)

        localImage.load(myUser.userImage, R.drawable.ic_incognito, true)
        localUserName.text = myUser.userName

        visitorImage.load(fallbackImage = R.drawable.ic_incognito)
        visitorImage.setOnClickListener {
            userSelectorDialog = UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR, visitor)
            userSelectorDialog?.show(it.context)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.new_match_record)
            .setView(newMatchDialogView)
            .setPositiveButtonText(R.string.add)
            .setPositiveButtonListener {
                validateMatchMembersList()
            }
            .setNegativeButtonText(R.string.close)
            .setNegativeButtonListener {
                dialog?.dismiss()
            }
            .setOnDismissListener(DialogInterface.OnDismissListener {
                userSelectorBus.unregister(this)
            })
            .build(context)
        dialog?.show()
    }

    private fun validateMatchMembersList() {
        dialog?.context?.let { context ->
            visitor?.let {
                try {
                    openDateSelectionDialog(
                        context,
                        MatchRecord(
                            local = myUser,
                            visitor = visitor,
                            localScore = localScore.text.toString().toInt(),
                            visitorScore = visitorScore.text.toString().toInt()
                        )
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, R.string.invalid_score, Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, R.string.select_user, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEligibleUsers(): MutableList<User> {
        val eligibleUsers = mutableListOf<User>()
        eligibleUsers.addAll(
            users.filterNot {
                it.userId in arrayOf(myUser.userId, visitor?.userId)
            }
        )
        return eligibleUsers
    }

    @Subscribe
    fun onUserSelected(event: UserSelectorDialog.UserSelectedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                visitor = event.user
                visitorImage.load(url = event.user.userImage, rounded = true, fallbackImage = R.drawable.ic_incognito)
                visitorUserName.text = event.user.userName
            }
            else -> return
        }
    }

    @Subscribe
    fun onUserRemoved(event: UserSelectorDialog.UserRemovedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                visitor = null
                visitorImage.load(fallbackImage = R.drawable.ic_incognito)
                visitorUserName.setText(R.string.visitor)
            }
            else -> return
        }
    }

    private fun openDateSelectionDialog(context: Context, match: MatchRecord) {
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

    fun refreshUsersList() {
        userSelectorDialog?.refreshUsersList(getEligibleUsers())
    }

    class CreateNewChampionshipMatchButtonClickedEvent(val match: MatchRecord)
}