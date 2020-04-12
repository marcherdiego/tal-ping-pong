package com.tal.android.pingpong.utils

import java.lang.Exception

fun String?.toInt(): Int {
    return if (this == null) {
        0
    } else {
        try {
            Integer.parseInt(this)
        } catch (e: Exception) {
            0
        }
    }
}
