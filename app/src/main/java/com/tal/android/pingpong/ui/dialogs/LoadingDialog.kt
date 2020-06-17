package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.tal.android.pingpong.R
import com.tal.android.pingpong.utils.DialogFactory

class LoadingDialog : BaseDialog() {

    fun show(context: Context): LoadingDialog {
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.loading_dialog, null)

        dialog = DialogFactory
            .Builder()
            .setCancelable(false)
            .setAutoDismiss(false)
            .setView(challengeDialogView)
            .build(context)
        dialog?.show()
        return this
    }
}