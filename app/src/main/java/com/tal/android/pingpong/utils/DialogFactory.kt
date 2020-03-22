package com.tal.android.pingpong.utils

import android.content.Context
import android.content.DialogInterface.OnCancelListener
import android.content.DialogInterface.OnDismissListener
import android.view.View
import androidx.appcompat.app.AlertDialog

object DialogFactory {
    class Builder {

        private var view: View? = null

        private var cancelable: Boolean = true

        private var title: CharSequence? = null
        private var titleResId: Int = 0

        private var message: CharSequence? = null
        private var messageResId: Int = 0

        private var onDismissListener: OnDismissListener? = null
        private var onCancelListener: OnCancelListener? = null

        private var positiveButtonListener: () -> Unit = {}
        private var positiveButtonText: CharSequence? = null
        private var positiveButtonTextResId: Int = 0

        private var negativeButtonListener: () -> Unit = {}
        private var negativeButtonText: CharSequence? = null
        private var negativeButtonTextResId: Int = 0

        fun setCancelable(cancelable: Boolean = true): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setTitle(title: CharSequence?): Builder {
            this.title = title
            return this
        }

        fun setTitle(title: Int?): Builder {
            titleResId = title ?: 0
            return this
        }

        fun setMessage(message: CharSequence?): Builder {
            this.message = message
            return this
        }

        fun setMessage(message: Int?): Builder {
            messageResId = message ?: 0
            return this
        }

        fun setOnDismissListener(onDismissListener: OnDismissListener?): Builder {
            this.onDismissListener = onDismissListener
            return this
        }

        fun setOnCancelListener(onCancelListener: OnCancelListener?): Builder {
            this.onCancelListener = onCancelListener
            return this
        }

        fun setPositiveButtonListener(callback: () -> Unit): Builder {
            this.positiveButtonListener = callback
            return this
        }

        fun setPositiveButtonText(positiveButtonText: CharSequence?): Builder {
            this.positiveButtonText = positiveButtonText
            return this
        }

        fun setPositiveButtonText(positiveButtonText: Int?): Builder {
            positiveButtonTextResId = positiveButtonText ?: 0
            return this
        }

        fun setNegativeButtonListener(callback: () -> Unit): Builder {
            this.negativeButtonListener = callback
            return this
        }

        fun setNegativeButtonText(negativeButtonText: CharSequence?): Builder {
            this.negativeButtonText = negativeButtonText
            return this
        }

        fun setNegativeButtonText(negativeButtonText: Int?): Builder {
            negativeButtonTextResId = negativeButtonText ?: 0
            return this
        }

        fun setView(view: View?): Builder {
            this.view = view
            return this
        }

        fun build(context: Context): AlertDialog {
            val builder = AlertDialog
                .Builder(context)
                .setCancelable(cancelable)
                .setView(view)
                .setTitle(getString(context, title, titleResId))
                .setMessage(getString(context, message, messageResId))
                .setOnDismissListener(onDismissListener)
                .setOnCancelListener(onCancelListener)
            getString(context, positiveButtonText, positiveButtonTextResId)?.let {
                builder.setPositiveButton(it) { _, _ -> positiveButtonListener() }
            }
            getString(context, negativeButtonText, negativeButtonTextResId)?.let {
                builder.setNegativeButton(it) { _, _ -> negativeButtonListener() }
            }
            return builder.create()
        }

        private fun getString(context: Context, value: CharSequence?, resId: Int): CharSequence? {
            if (value != null) {
                return value
            }
            if (resId != 0) {
                return context.getString(resId)
            }
            return null
        }
    }
}
