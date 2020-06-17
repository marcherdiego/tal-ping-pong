package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputEditText
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.DialogFactory

class DeclineMatchDialog(private val match: MatchRecord, private val bus: Bus) : BaseDialog() {

    fun show(context: Context) {
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.decline_challenge_dialog, null)
        val declineReasons: TextInputEditText = challengeDialogView.findViewById(R.id.reasons)

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.incoming_challenge)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.confirm)
            .setNegativeButtonText(R.string.cancel)
            .setPositiveButtonListener {
                bus.post(ConfirmDeclineMatchButtonClickedEvent(match, declineReasons.text.toString()))
            }
            .build(context)
        dialog?.show()
    }

    class ConfirmDeclineMatchButtonClickedEvent(val match: MatchRecord, val declineReason: String)
}