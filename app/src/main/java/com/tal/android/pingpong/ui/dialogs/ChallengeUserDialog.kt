package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import java.util.*

class ChallengeUserDialog(private val user: User) {

    private lateinit var challengeDialogCallback: ChallengeDialogCallback

    fun show(context: Context, challengeDialogCallback: ChallengeDialogCallback) {
        this.challengeDialogCallback = challengeDialogCallback
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.challenges_user_dialog, null)
        val userImage = challengeDialogView.findViewById<ImageView>(R.id.user_image)
        val userName = challengeDialogView.findViewById<TextView>(R.id.user_name)
        val userEmail = challengeDialogView.findViewById<TextView>(R.id.user_email)
        val userStats = challengeDialogView.findViewById<TextView>(R.id.user_stats)

        GlideUtils.loadImage(user.userImage, userImage, R.drawable.ic_incognito, true)
        userName.text = user.userName
        userEmail.text = user.userEmail
        userStats.text = context.getString(R.string.user_stats, user.matchesWon, user.matchesLost, user.matchesRatio)

        val myEmail = SharedPreferencesUtils(context).getUser()?.userEmail

        val challengeDialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setTitle(R.string.user_details)
            .setView(challengeDialogView)
        if (user.userEmail != myEmail) {
            challengeDialogBuilder
                .setPositiveButtonText(R.string.challenge)
                .setPositiveButtonListener {
                    openChallengeDateSelectionDialog(context, user)
                }
        }
        challengeDialogBuilder
            .setNegativeButtonText(R.string.close)
            .build(context)
            .show()
    }

    private fun openChallengeDateSelectionDialog(context: Context, user: User) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                openChallengeTimeSelectionDialog(context, user, year, monthOfYear, dayOfMonth)
            },
            today[Calendar.YEAR],
            today[Calendar.MONTH],
            today[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun openChallengeTimeSelectionDialog(context: Context, user: User, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                try {
                    challengeDialogCallback.onChallengeUser(user, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                } catch (e: InvalidMatchTimeException) {
                    challengeDialogCallback.onInvalidTimeSelected()
                    openChallengeTimeSelectionDialog(context, user, year, monthOfYear, dayOfMonth)
                }
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    interface ChallengeDialogCallback {
        fun onChallengeUser(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int)
        fun onInvalidTimeSelected()
    }
}