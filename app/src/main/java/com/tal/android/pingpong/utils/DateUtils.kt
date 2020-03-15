package com.tal.android.pingpong.utils

import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DATE_FORMAT_US = "MMM dd, yyyy hh:mm a"
    private const val DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val format = SimpleDateFormat(DATE_INPUT_FORMAT)

    fun formatDate(date: String?): String? {
        if (date == null) {
            return null
        }
        try {
            return DateFormat.format(DATE_FORMAT_US, format.parse(date)).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun toDate(date: String?) : Date? {
        if (date == null) {
            return null
        }
        return format.parse(date)
    }
}