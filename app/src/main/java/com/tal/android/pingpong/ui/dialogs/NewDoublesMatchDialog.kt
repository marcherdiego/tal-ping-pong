package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load
import com.tal.android.pingpong.utils.multiLet
import org.greenrobot.eventbus.Subscribe
import java.util.*

class NewDoublesMatchDialog(private val users: List<User>, private val myUser: User, private val bus: Bus) {

    private var dialog: AlertDialog? = null
    private val userSelectorBus = Bus.newInstance

    private lateinit var localImage: ImageView
    private lateinit var localName: TextView

    private lateinit var localCompanionImage: ImageView
    private lateinit var localCompanionName: TextView

    private lateinit var visitorImage: ImageView
    private lateinit var visitorName: TextView

    private lateinit var visitorCompanionImage: ImageView
    private lateinit var visitorCompanionName: TextView

    private lateinit var difficultyBar: DifficultyBar

    private var localCompanion: User? = null
    private var visitor: User? = null
    private var visitorCompanion: User? = null

    fun show(context: Context) {
        userSelectorBus.register(this)
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.doubles_challenge_proposal_dialog, null)
        localImage = challengeDialogView.findViewById(R.id.local_1_image)
        localName = challengeDialogView.findViewById(R.id.local_1_name)

        localCompanionImage = challengeDialogView.findViewById(R.id.local_2_image)
        localCompanionName = challengeDialogView.findViewById(R.id.local_2_name)

        visitorImage = challengeDialogView.findViewById(R.id.visitor_1_image)
        visitorName = challengeDialogView.findViewById(R.id.visitor_1_name)

        visitorCompanionImage = challengeDialogView.findViewById(R.id.visitor_2_image)
        visitorCompanionName = challengeDialogView.findViewById(R.id.visitor_2_name)

        difficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        difficultyBar.setup(localRatio = myUser.matchesRatioValue)

        localImage.load(myUser.userImage, R.drawable.ic_incognito, true)
        localName.text = myUser.userName

        localCompanionImage.load(fallbackImage = R.drawable.ic_incognito)
        visitorImage.load(fallbackImage = R.drawable.ic_incognito)
        visitorCompanionImage.load(fallbackImage = R.drawable.ic_incognito)

        localCompanionImage.setOnClickListener {
            UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.LOCAL_COMPANION, localCompanion).show(it.context)
        }
        visitorImage.setOnClickListener {
            UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR, visitor).show(it.context)
        }
        visitorCompanionImage.setOnClickListener {
            UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR_COMPANION, visitorCompanion).show(it.context)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.new_doubles_match)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.challenge)
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
            multiLet(localCompanion, visitor, visitorCompanion) { localCompanion, visitor, visitorCompanion ->
                openDateSelectionDialog(
                    context,
                    MatchRecord(
                        local = myUser,
                        localCompanion = localCompanion,
                        visitor = visitor,
                        visitorCompanion = visitorCompanion
                    )
                )
            } ?: run {
                Toast.makeText(context, R.string.some_participants_are_missing, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEligibleUsers(): MutableList<User> {
        val eligibleUsers = mutableListOf<User>()
        eligibleUsers.addAll(
            users.filterNot {
                it.userId in arrayOf(
                    myUser.userId,
                    localCompanion?.userId,
                    visitor?.userId,
                    visitorCompanion?.userId
                )
            }
        )
        return eligibleUsers
    }

    @Subscribe
    fun onUserSelected(event: UserSelectorDialog.UserSelectedEvent) {
        val (userImageView, userNameTextView) = when (event.userType) {
            UserSelectorDialog.LOCAL_COMPANION -> {
                localCompanion = event.user
                Pair(localCompanionImage, localCompanionName)
            }
            UserSelectorDialog.VISITOR -> {
                visitor = event.user
                Pair(visitorImage, visitorName)
            }
            UserSelectorDialog.VISITOR_COMPANION -> {
                visitorCompanion = event.user
                Pair(visitorCompanionImage, visitorCompanionName)
            }
            else -> return
        }
        userImageView.load(url = event.user.userImage, rounded = true, fallbackImage = R.drawable.ic_incognito)
        userNameTextView.text = event.user.userName
        updateDifficultyBar()
    }

    @Subscribe
    fun onUserRemoved(event: UserSelectorDialog.UserRemovedEvent) {
        val (userImageView, userNameTextView) = when (event.userType) {
            UserSelectorDialog.LOCAL_COMPANION -> {
                localCompanion = null
                Pair(localCompanionImage, localCompanionName)
            }
            UserSelectorDialog.VISITOR -> {
                visitor = null
                Pair(visitorImage, visitorName)
            }
            UserSelectorDialog.VISITOR_COMPANION -> {
                visitorCompanion = null
                Pair(visitorCompanionImage, visitorCompanionName)
            }
            else -> return
        }
        userImageView.load(fallbackImage = R.drawable.ic_incognito)
        userNameTextView.text = null
        updateDifficultyBar()
    }

    private fun updateDifficultyBar() {
        difficultyBar.setup(
            localRatio = myUser.matchesRatioValue,
            localCompanionRatio = localCompanion?.matchesRatioValue,
            visitorRatio = visitor?.matchesRatioValue,
            visitorCompanionRatio = visitorCompanion?.matchesRatioValue
        )
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
                try {
                    val matchDate = getMatchDate(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    match.matchDate = matchDate.toString()
                    dialog?.dismiss()
                    bus.post(CreateNewDoublesMatchButtonClickedEvent(match))
                } catch (e: InvalidMatchTimeException) {
                    bus.post(NewDoublesMatchInvalidTimeSelectedEvent())
                    openTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
                }
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    @Throws(InvalidMatchTimeException::class)
    private fun getMatchDate(year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int): Date {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidMatchTimeException()
        }
        return selectedDateTime.time
    }

    class CreateNewDoublesMatchButtonClickedEvent(val match: MatchRecord)
    class NewDoublesMatchInvalidTimeSelectedEvent
}