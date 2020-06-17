package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.tal.android.pingpong.utils.load
import java.util.*

class NewSinglesMatchDialog(private val myUser: User, private val rivalUser: User, private val bus: Bus) : BaseDialog() {

    fun show(context: Context) {
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.challenges_user_dialog, null)
        val userImage = challengeDialogView.findViewById<ImageView>(R.id.user_image)
        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        val userName = challengeDialogView.findViewById<TextView>(R.id.user_name)
        val userEmail = challengeDialogView.findViewById<TextView>(R.id.user_email)
        val userStats = challengeDialogView.findViewById<TextView>(R.id.user_stats)

        difficultyBar.setup(myUser.matchesRatioValue, rivalUser.matchesRatioValue)
        userImage.load(rivalUser.userImage, R.drawable.ic_incognito, true)
        userName.text = rivalUser.userName
        userEmail.text = rivalUser.userEmail
        userStats.text = context.getString(R.string.user_stats, rivalUser.matchesWon, rivalUser.matchesLost, rivalUser.matchesRatio)

        val myEmail = SharedPreferencesUtils(context).getUser()?.userEmail

        val challengeDialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setTitle(R.string.user_details)
            .setView(challengeDialogView)
        if (rivalUser.userEmail != myEmail) {
            challengeDialogBuilder
                .setPositiveButtonText(R.string.challenge)
                .setPositiveButtonListener {
                    openChallengeDateSelectionDialog(context, MatchRecord(local = myUser, visitor = rivalUser))
                }
        }
        dialog = challengeDialogBuilder
            .setNegativeButtonText(R.string.close)
            .build(context)
        dialog?.show()
    }

    private fun openChallengeDateSelectionDialog(context: Context, match: MatchRecord) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                openChallengeTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
            },
            today[Calendar.YEAR],
            today[Calendar.MONTH],
            today[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun openChallengeTimeSelectionDialog(context: Context, match: MatchRecord, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                try {
                    val matchDate = getMatchDate(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    match.matchDate = matchDate.toString()
                    dialog?.dismiss()
                    bus.post(CreateNewSinglesMatchButtonClickedEvent(match))
                } catch (e: InvalidMatchTimeException) {
                    bus.post(NewSinglesMatchInvalidTimeSelectedEvent())
                    openChallengeTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
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

    class CreateNewSinglesMatchButtonClickedEvent(val match: MatchRecord)
    class NewSinglesMatchInvalidTimeSelectedEvent
}