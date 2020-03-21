package com.tal.android.pingpong.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.activities.MainActivity

object NotificationsManager {
    private val gson = Gson()
    private val mapType = object : TypeToken<Map<String, String>>() {}.type

    const val KEY_NOTIFICATION_TITLE = "title"
    const val KEY_NOTIFICATION_BODY = "body"
    const val KEY_NOTIFICATION_DATA = "data"
    const val KEY_NOTIFICATION_TYPE = "notification_type"

    fun showNotification(context: Context, data: Map<String, String>) {
        val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_racket_icon)
        val notificationData: Map<String, String> = gson.fromJson(data[KEY_NOTIFICATION_DATA], mapType)
        val builder = NotificationCompat
            .Builder(context, ChannelDescriptors.highImportanceChannel.channelId)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.drawable.ic_racket_icon)
            .setLargeIcon(logoBitmap)
            .setContentTitle(data[KEY_NOTIFICATION_TITLE])
            .setContentText(data[KEY_NOTIFICATION_BODY])
            .setContentIntent(buildNotificationPendingIntent(context, data))
            .setStyle(NotificationCompat.BigTextStyle().bigText(data[KEY_NOTIFICATION_BODY]))
            .setVibrate(LongArray(0))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        NotificationManagerCompat
            .from(context)
            .notify(notificationData[KEY_NOTIFICATION_TYPE].hashCode(), builder.build())
    }

    private fun buildNotificationPendingIntent(context: Context, data: Map<String, String>): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context.applicationContext)
        stackBuilder.addNextIntentWithParentStack(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    // Creates a NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    fun createNotificationChannel(context: Context, channelDescriptor: ChannelDescriptor) {
        val channel = NotificationChannel(channelDescriptor.channelId, channelDescriptor.channelName, channelDescriptor.channelImportance)
        channel.description = channelDescriptor.channelDescription
        // Register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}