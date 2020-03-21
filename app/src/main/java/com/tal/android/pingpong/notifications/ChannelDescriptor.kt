package com.tal.android.pingpong.notifications

import android.annotation.TargetApi
import android.app.NotificationManager
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi

/**
 * This class as its name indicates, contains then description of a notification channel.
 *
 * Keep in mind that the [channelImportance] is available for API level [VERSION_CODES.N] and above
 */
data class ChannelDescriptor(val channelId: String, val channelName: String, val channelDescription: String) {

    @TargetApi(VERSION_CODES.N)
    @RequiresApi(VERSION_CODES.N)
    var channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT
}
