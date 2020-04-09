package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import java.util.*

class NewDoublesMatchDialog(private val myUser: User, private val bus: Bus) {

    fun show(context: Context) {
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.doubles_challenge_proposal_dialog, null)
        val localImage = challengeDialogView.findViewById<ImageView>(R.id.local_1_image)
        val localCompanionImage = challengeDialogView.findViewById<ImageView>(R.id.local_2_image)
        val visitorImage = challengeDialogView.findViewById<ImageView>(R.id.visitor_1_image)
        val visitorCompanionImage = challengeDialogView.findViewById<ImageView>(R.id.visitor_2_image)

        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        difficultyBar.setup(local = myUser)

        GlideUtils.loadImage(myUser.userImage, localImage, R.drawable.ic_incognito, true)
        GlideUtils.loadImage(imageView = localCompanionImage, fallbackImage = R.drawable.ic_incognito)
        GlideUtils.loadImage(imageView = visitorImage, fallbackImage = R.drawable.ic_incognito)
        GlideUtils.loadImage(imageView = visitorCompanionImage, fallbackImage = R.drawable.ic_incognito)

        DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.user_details)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.challenge)
            .setPositiveButtonListener {
                openDateSelectionDialog(context, MatchRecord())
            }
            .setNegativeButtonText(R.string.close)
            .build(context)
            .show()
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
                    bus.post(CreateNewDoublesMatchButtonClickedEvent(match, matchDate))
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

    class CreateNewDoublesMatchButtonClickedEvent(val match: MatchRecord, val matchDate: Date)

    class NewDoublesMatchInvalidTimeSelectedEvent
}