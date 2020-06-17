package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.*

class MatchEditDialog(private val match: MatchRecord, private val bus: Bus) : BaseDialog() {

    private lateinit var localScore: EditText
    private lateinit var visitorScore: EditText

    fun show(activity: Context) {
        val matchEditDialogView = LayoutInflater
            .from(activity)
            .inflate(
                if (match.isSinglesMatch()) {
                    R.layout.singles_match_edit_dialog
                } else {
                    R.layout.doubles_match_edit_dialog
                },
                null
            )

        localScore = matchEditDialogView.findViewById(R.id.local_score)
        visitorScore = matchEditDialogView.findViewById(R.id.visitor_score)

        val localUserImage: ImageView = matchEditDialogView.findViewById(R.id.local_image)
        val local: TextView = matchEditDialogView.findViewById(R.id.local)
        val localCompanionUserImage: ImageView? = matchEditDialogView.findViewById(R.id.local_companion_image)
        val localCompanion: TextView? = matchEditDialogView.findViewById(R.id.local_companion)
        val visitorUserImage: ImageView = matchEditDialogView.findViewById(R.id.visitor_image)
        val visitor: TextView = matchEditDialogView.findViewById(R.id.visitor)
        val visitorCompanionUserImage: ImageView? = matchEditDialogView.findViewById(R.id.visitor_companion_image)
        val visitorCompanion: TextView? = matchEditDialogView.findViewById(R.id.visitor_companion)

        val oldLocalScore: TextView = matchEditDialogView.findViewById(R.id.old_local_score)
        val oldVisitorScore: TextView = matchEditDialogView.findViewById(R.id.old_visitor_score)

        val matchDate: TextView = matchEditDialogView.findViewById(R.id.match_date)

        local.text = match.local?.userName
        visitor.text = match.visitor?.userName
        localUserImage.load(match.local?.userImage, R.drawable.ic_incognito, true)
        visitorUserImage.load(match.visitor?.userImage, R.drawable.ic_incognito, true)

        if (match.isSinglesMatch().not()) {
            localCompanion?.text = match.localCompanion?.userName
            visitorCompanion?.text = match.visitorCompanion?.userName
            localCompanionUserImage.load(match.localCompanion?.userImage, R.drawable.ic_incognito, true)
            visitorCompanionUserImage.load(match.visitorCompanion?.userImage, R.drawable.ic_incognito, true)
        }

        oldLocalScore.visibility = View.GONE
        oldVisitorScore.visibility = View.GONE

        localScore.setText(match.localScore.toString())
        visitorScore.setText(match.visitorScore.toString())

        val scoreTextListener: (text: Editable?) -> Unit = {
            dialog
                ?.getButton(DialogInterface.BUTTON_POSITIVE)
                ?.setText(
                    if (match.hasTempRequestChanges(localScore.toInt(), visitorScore.toInt())) {
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
            .setView(matchEditDialogView)
            .setNeutralButtonText(R.string.cancel)
            .setPositiveButtonListener {
                if (match.hasTempRequestChanges(localScore.toInt(), visitorScore.toInt())) {
                    val matchCopy = match.copy()
                    matchCopy.localScore = localScore.toInt()
                    matchCopy.visitorScore = visitorScore.toInt()
                    bus.post(MatchEditButtonClickedEvent(matchCopy))
                } else {
                    dismiss()
                }
            }
        if (match.hasRequestedChanges == false) {
            dialogBuilder.setPositiveButtonText(R.string.close)
        } else {
            dialogBuilder
                .setPositiveButtonText(R.string.accept)
                .setNegativeButtonText(R.string.decline)
                .setNegativeButtonListener {
                    bus.post(DeclineMatchEditButtonClickedEvent(match))
                }
        }
        dialog = dialogBuilder.build(activity)
        dialog?.show()
    }

    class MatchEditButtonClickedEvent(val match: MatchRecord)
    class DeclineMatchEditButtonClickedEvent(val match: MatchRecord)
}