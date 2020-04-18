package com.tal.android.pingpong.utils

fun Float.asString(digits: Int = 2) = String.format("%.${digits}f", this)

fun Float.asPercentString(digits: Int = 0) = String.format("%.${digits}f%%", this)