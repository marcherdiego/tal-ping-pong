package com.tal.android.pingpong.utils

fun Float.asPercentString(digits: Int = 0) = String.format("%.${digits}f%%", this)