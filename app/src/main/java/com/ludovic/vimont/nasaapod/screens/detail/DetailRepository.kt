package com.ludovic.vimont.nasaapod.screens.detail

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.FutureTarget
import com.ludovic.vimont.nasaapod.api.glide.GlideDispatchProgressListener
import com.ludovic.vimont.nasaapod.db.PhotoDao
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.ui.BitmapRequestListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class DetailRepository(private val photoDao: PhotoDao,
                       private val glide: RequestManager,
                       private val contentResolver: ContentResolver) {
    private var currentFutureTarget: FutureTarget<Bitmap>? = null

    suspend fun getPhoto(photoId: String): Photo {
        return photoDao.get(photoId)
    }

    fun fetchBitmap(imageURL: String): StateData<Bitmap> {
        var stateData: StateData<Bitmap> = StateData.loading()
        try {
            currentFutureTarget = glide.asBitmap().load(imageURL).listener(BitmapRequestListener {
                stateData = it
                GlideDispatchProgressListener.remove(imageURL)
            }).submit()
            currentFutureTarget?.get()
        } catch (exception: Exception) {
            val errorMessage = "An error occurred while fetching the image. Check if you are connected to internet and retry."
            stateData = StateData.error(errorMessage)
        }
        return stateData
    }

    fun cancelBitmapDownload() {
        currentFutureTarget?.cancel(true)
    }

    fun saveImage(bitmap: Bitmap, folder: String, name: String) {
        val fileOutputStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/$folder/"
            )
            val imageUri: Uri? = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            imageUri?.let {
                contentResolver.openOutputStream(imageUri)
            }
        } else {
            val pictureDirectory = Environment.DIRECTORY_PICTURES
            val imagesDir: String = Environment.getExternalStoragePublicDirectory(pictureDirectory).toString()
            val picturesFolder = File(imagesDir, folder)
            if (!picturesFolder.exists()) {
                picturesFolder.mkdir()
            }
            val image = File(picturesFolder.toString(), name)
            FileOutputStream(image)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream?.close()
    }
}