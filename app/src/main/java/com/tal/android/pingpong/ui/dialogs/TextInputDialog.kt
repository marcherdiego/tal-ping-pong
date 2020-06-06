package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.utils.DialogFactory

class TextInputDialog(private val bus: Bus) {

    private var dialog: AlertDialog? = null

    fun show(context: Context, @StringRes title: Int, @StringRes positiveButtonText: Int, @StringRes negativeButtonText: Int,
             @StringRes hint: Int = 0, preloadedInput: String? = null, requestCode: Int = 0) {
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.text_input_dialog, null)
        val inputLayout: TextInputLayout = challengeDialogView.findViewById(R.id.input_layout)
        val userInput: TextInputEditText = challengeDialogView.findViewById(R.id.input)
        userInput.setText(preloadedInput)
        if (hint != 0) {
            inputLayout.hint = context.getString(hint)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setTitle(title)
            .setView(challengeDialogView)
            .setPositiveButtonText(positiveButtonText)
            .setNegativeButtonText(negativeButtonText)
            .setPositiveButtonListener {
                bus.post(PositiveButtonClickedEvent(requestCode, userInput.text.toString()))
            }
            .build(context)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    class PositiveButtonClickedEvent(val requestCode: Int, val input: String)
}