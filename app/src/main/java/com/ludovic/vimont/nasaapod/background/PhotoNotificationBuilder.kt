package com.ludovic.vimont.nasaapod.background

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ludovic.vimont.nasaapod.BuildConfig
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.MainActivity

/**
 * Used to display a media friendly notification to allow the user to interact with the current
 * MediaSession.
 * @see: https://stackoverflow.com/questions/21872022/notification-for-android-music-player
 */
class PhotoNotificationBuilder {
    companion object {
        const val NOTIFICATION_ID = 15_951

        private const val CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.NasaApod"
        private const val CHANNEL_NAME = "Astronomy Picture of the Day"
        private const val CHANNEL_DESCRIPTION = "Daily display the astronomical photo of the day from the NASA API."
    }

    fun showNotification(context: Context, photo: Photo, largeIcon: Bitmap) {
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationCompat: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(photo.title)
                .setContentText(photo.explanation)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(getContentIntent(context, photo))
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

    private fun getContentIntent(context: Context, photo: Photo): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.KEY_PHOTO_DATE, photo.date)
        intent.putExtra(MainActivity.KEY_OPEN_DETAIL_FRAGMENT, true)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}