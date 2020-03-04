package com.tal.android.pingpong.utils

import android.text.format.DateFormat
import java.util.*

object DateUtils {

    private const val DATE_FORMAT_US = "MMM dd, yyyy hh:mm a"

    fun formatDate(date: Date?): String? {
        if (date == null) {
            return null
        }
        return DateFormat.format(DATE_FORMAT_US, date).toString()
    }
}