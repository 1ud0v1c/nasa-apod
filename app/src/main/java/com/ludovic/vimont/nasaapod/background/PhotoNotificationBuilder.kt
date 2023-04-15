package com.ludovic.vimont.nasaapod.background

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.ludovic.vimont.nasaapod.BuildConfig
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.detail.DetailFragmentArgs

class PhotoNotificationBuilder(private val notificationManager: NotificationManager,
                               private val notificationCompatBuilder: NotificationCompat.Builder,
                               private val navDeepLinkBuilder: NavDeepLinkBuilder) {
    companion object {
        const val NOTIFICATION_ID = 15_951
        const val CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.NasaApod"

        private const val CHANNEL_NAME = "Astronomy Picture of the Day"
        private const val CHANNEL_DESCRIPTION = "Daily display the astronomical photo of the day from the NASA API."
    }

    fun showNotification(photo: Photo, largeIcon: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationCompat: NotificationCompat.Builder =
            notificationCompatBuilder.setContentTitle(photo.title)
                .setContentText(photo.explanation)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                )
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(getContentIntent(photo))
                .setAutoCancel(true)

        val notification: Notification = notificationCompat.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    @SuppressLint("NewApi")
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        channel.description = CHANNEL_DESCRIPTION
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.setSound(null, null)
        channel.enableVibration(false)
        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
    }

    private fun getContentIntent(photo: Photo): PendingIntent {
        return navDeepLinkBuilder.setGraph(R.navigation.navigation_graph)
            .setDestination(R.id.detailFragment)
            .setArguments(DetailFragmentArgs(photo.date).toBundle())
            .createPendingIntent()
    }
}