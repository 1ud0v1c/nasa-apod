package com.ludovic.vimont.nasaapod.helper

import android.content.Context
import android.content.Intent

object IntentHelper {
    private const val SHARE_TYPE = "text/plain"

    fun shareLink(context: Context, url: String, subject: String, title: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = SHARE_TYPE
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(sharingIntent, title).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}