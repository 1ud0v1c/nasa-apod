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

    @Query("SELECT * FROM photo WHERE photoId=:photoId")
    suspend fun get(photoId: Int): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photos: List<Photo>)
}