package com.ludovic.vimont.nasaapod.db

import androidx.room.*
import com.ludovic.vimont.nasaapod.model.Photo

/**
 * Responsible of all the access made to the photo table.
 * @see Photo
 */
@Dao
interface PhotoDao {
    @Query("SELECT count(photoId) FROM photo")
    suspend fun count(): Int

    @Query("SELECT * FROM photo")
    suspend fun getAll(): List<Photo>

    @Query("SELECT * FROM photo WHERE date=:date")
    suspend fun get(date: String): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photos: List<Photo>)
}