package com.tal.android.pingpong.ui.dialogs

import android.view.View
import androidx.appcompat.app.AlertDialog
import com.tal.android.pingpong.utils.ALPHA_30
import com.tal.android.pingpong.utils.DEFAULT_ALPHA

open class BaseDialog {

    protected var dialog: AlertDialog? = null

    fun getConfirmedVisibility(confirmed: Boolean?) = if (confirmed == true) {
        View.VISIBLE
    } else {
        View.GONE
    }

    fun getUserImageAlpha(confirmed: Boolean?) = if (confirmed == true) {
        DEFAULT_ALPHA
    } else {
        ALPHA_30
    }

    open fun dismiss() {
        dialog?.dismiss()
    }
}