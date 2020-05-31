package com.tal.android.pingpong.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.Exception

fun EditText?.toInt(): Int {
    return if (this == null) {
        0
    } else {
        try {
            text.toString().toInt()
        } catch (e: Exception) {
            0
        }
    }
}

fun EditText?.onTextChanged(block: (s: Editable?) -> Unit) {
    this?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}
