package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Guideline
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

    fun show(activity: Context) {
        val challengeDialogView = LayoutInflater
            .from(activity)
            .inflate(R.layout.challenge_edit_dialog, null)
        val localUserImage = challengeDialogView.findViewById<ImageView>(R.id.local_image)
        val local = challengeDialogView.findViewById<TextView>(R.id.local)
        val localScore = challengeDialogView.findViewById<EditText>(R.id.local_score)
        val matchDate = challengeDialogView.findViewById<TextView>(R.id.match_date)
        val visitorUserImage = challengeDialogView.findViewById<ImageView>(R.id.visitor_image)
        val visitor = challengeDialogView.findViewById<TextView>(R.id.visitor)
        val visitorScore = challengeDialogView.findViewById<EditText>(R.id.visitor_score)

        val localUser = match.local ?: return
        val visitorUser = match.visitor ?: return

        local.text = match.local?.userName
        localScore.setText(match.localScore.toString())
        visitor.text = match.visitor?.userName
        visitorScore.setText(match.visitorScore.toString())
        GlideUtils.loadImage(localUser.userImage, localUserImage, R.drawable.ic_incognito, true)
        GlideUtils.loadImage(visitorUser.userImage, visitorUserImage, R.drawable.ic_incognito, true)

        matchDate.text = DateUtils.formatDate(match.matchDate)

        val dialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.edit_match_details)
            .setView(challengeDialogView)
            .setNeutralButtonText(R.string.cancel)
            .setPositiveButtonListener {
                val matchCopy = match.copy()
                matchCopy.localScore = localScore.toInt()
                matchCopy.visitorScore = visitorScore.toInt()
                bus.post(AcceptMatchEditButtonClickedEvent(matchCopy))
            }
        if (isEditor.not()) {
            dialogBuilder
                .setPositiveButtonText(R.string.accept)
                .setNegativeButtonText(R.string.decline)
                .setNegativeButtonListener {
                    bus.post(DeclineMatchEditButtonClickedEvent())
                }
        } else {
            dialogBuilder.setPositiveButtonText(R.string.edit)
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