package com.tal.android.pingpong.utils

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