package com.ludovic.vimont.nasaapod.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ludovic.vimont.nasaapod.model.Photo

/**
 * Handle the creation of the database by extending RoomDatabase.
 */
@Database(entities = [Photo::class], version = 1, exportSchema = true)
abstract class PhotoDatabase: RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "nasa_apod_database"
    }

    abstract fun photoDao(): PhotoDao
}