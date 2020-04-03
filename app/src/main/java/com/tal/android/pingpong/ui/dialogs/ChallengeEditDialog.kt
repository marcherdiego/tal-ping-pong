package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.addTextChangedListener
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.utils.*
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min

class ChallengeEditDialog(private val match: MatchRecord, private val bus: Bus, private val isEditor: Boolean = true) {

    private var dialog: AlertDialog? = null

    private lateinit var localScore: EditText
    private lateinit var visitorScore: EditText

    fun show(activity: Context) {
        val challengeDialogView = LayoutInflater
            .from(activity)
            .inflate(R.layout.challenge_edit_dialog, null)

        localScore = challengeDialogView.findViewById(R.id.local_score)
        visitorScore = challengeDialogView.findViewById(R.id.visitor_score)

        val localUserImage: ImageView = challengeDialogView.findViewById(R.id.local_image)
        val local: TextView = challengeDialogView.findViewById(R.id.local)
        val oldLocalScore: TextView = challengeDialogView.findViewById(R.id.old_local_score)
        val matchDate: TextView = challengeDialogView.findViewById(R.id.match_date)
        val visitorUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_image)
        val visitor: TextView = challengeDialogView.findViewById(R.id.visitor)
        val oldVisitorScore: TextView = challengeDialogView.findViewById(R.id.old_visitor_score)

        val localUser = match.local ?: return
        val visitorUser = match.visitor ?: return

        local.text = match.local?.userName
        visitor.text = match.visitor?.userName
        GlideUtils.loadImage(localUser.userImage, localUserImage, R.drawable.ic_incognito, true)
        GlideUtils.loadImage(visitorUser.userImage, visitorUserImage, R.drawable.ic_incognito, true)

        if (match.hasRequestedChanges == true) {
            oldLocalScore.paintFlags = oldLocalScore.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            oldVisitorScore.paintFlags = oldVisitorScore.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            localScore.setText(match.requestedLocalScore.toString())
            visitorScore.setText(match.requestedVisitorScore.toString())

            oldLocalScore.text = match.localScore.toString()
            oldVisitorScore.text = match.visitorScore.toString()
        } else {
            oldLocalScore.visibility = View.GONE
            oldVisitorScore.visibility = View.GONE

            localScore.setText(match.localScore.toString())
            visitorScore.setText(match.visitorScore.toString())
        }

        val scoreTextListener: (text: Editable?) -> Unit = {
            dialog
                ?.getButton(DialogInterface.BUTTON_POSITIVE)
                ?.setText(
                    if (match.hasScoreRequestChanges(localScore.toInt(), visitorScore.toInt())) {
                        R.string.edit
                    } else {
                        R.string.close
                    }
                )
        }
        localScore.addTextChangedListener(afterTextChanged = scoreTextListener)
        visitorScore.addTextChangedListener(afterTextChanged = scoreTextListener)

        matchDate.text = DateUtils.formatDate(match.matchDate)

        val dialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.edit_match_details)
            .setView(challengeDialogView)
            .setNeutralButtonText(R.string.cancel)
            .setPositiveButtonListener {
                if (match.hasScoreRequestChanges(localScore.toInt(), visitorScore.toInt())) {
                    val matchCopy = match.copy()
                    matchCopy.localScore = localScore.toInt()
                    matchCopy.visitorScore = visitorScore.toInt()
                    bus.post(AcceptMatchEditButtonClickedEvent(matchCopy))
                } else {
                    dismiss()
                }
            }
        if (isEditor) {
            dialogBuilder.setPositiveButtonText(R.string.close)
        } else {
            dialogBuilder
                .setPositiveButtonText(R.string.accept)
                .setNegativeButtonText(R.string.decline)
                .setNegativeButtonListener {
                    bus.post(DeclineMatchEditButtonClickedEvent())
                }
        }
        dialog = dialogBuilder.build(activity)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    class AcceptMatchEditButtonClickedEvent(val match: MatchRecord)
    class DeclineMatchEditButtonClickedEvent
}