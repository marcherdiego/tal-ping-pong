package com.tal.android.pingpong.notifications

import android.app.NotificationManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

/**
 * This object contains the channels definitions
 * Add here any channel you need to declare in oder to handle notifications in different groups or priorities
 */
object ChannelDescriptors {
    /**
     * This is the default channel. It is intended to be used for the most common type of notifications
     * It has default importance.
     */
    private val defaultChannel = ChannelDescriptor(
            channelId = "DefaultChannelId",
            channelName = "Default channel",
            channelDescription = "This channel handles normal notifications"
    ).apply {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT
        }
    }

    /**
     * This is the default channel. It is intended to be used for the most common type of notifications
     * It has default importance.
     */
    val highImportanceChannel = ChannelDescriptor(
            channelId = "HighImportanceChannelId",
            channelName = "High importance channel",
            channelDescription = "This channel handles high-importance notifications"
    ).apply {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            channelImportance = NotificationManager.IMPORTANCE_HIGH
        }
    }

    val channelsList = mutableListOf(defaultChannel, highImportanceChannel)
}
