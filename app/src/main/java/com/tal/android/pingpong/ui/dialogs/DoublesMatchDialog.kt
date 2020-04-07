package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class DoublesMatchDialog(private val myUser: User) {
    private lateinit var challengeDialogCallback: ChallengeDialogCallback

    fun show(context: Context, challengeDialogCallback: ChallengeDialogCallback) {
        this.challengeDialogCallback = challengeDialogCallback
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
            .setTitle(R.string.user_details)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.challenge)
            .setPositiveButtonListener {
                openChallengeDateSelectionDialog(context, MatchRecord())
            }
            .setNegativeButtonText(R.string.close)
            .build(context)
            .show()
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
                    challengeDialogCallback.onChallengeUser(match, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                } catch (e: InvalidMatchTimeException) {
                    challengeDialogCallback.onInvalidTimeSelected()
                    openChallengeTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
                }
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    interface ChallengeDialogCallback {
        fun onChallengeUser(match: MatchRecord, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int)
        fun onInvalidTimeSelected()
    }
}