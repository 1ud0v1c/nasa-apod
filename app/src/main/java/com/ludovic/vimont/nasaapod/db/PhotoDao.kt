package com.ludovic.vimont.nasaapod.db

import androidx.room.*
import com.ludovic.vimont.nasaapod.model.Photo

/**
 * Responsible of all the access made to the photo table.
 * @see Photo
 */
@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo")
    suspend fun getAll(): List<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photos: List<Photo>)
}