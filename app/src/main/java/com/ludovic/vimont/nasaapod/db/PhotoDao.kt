package com.ludovic.vimont.nasaapod.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ludovic.vimont.nasaapod.model.Photo

/**
 * Responsible of all the access made to the photo table.
 * @see Photo
 */
@Dao
interface PhotoDao {
    @Query("SELECT count(date) FROM photo")
    suspend fun count(): Int

    @Query("SELECT * FROM photo ORDER BY date DESC")
    suspend fun getAll(): List<Photo>

    @Query("SELECT * FROM photo WHERE date=:date")
    suspend fun get(date: String): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photos: List<Photo>)

    @Query("DELETE FROM photo")
    suspend fun drop()
}