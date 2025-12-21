package com.m4ykey.albly.track.data.local.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m4ykey.albly.track.data.local.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track : List<TrackEntity>)

    @Query("SELECT * FROM track_table WHERE id = :id")
    fun getTrackById(id : String) : List<TrackEntity>

    @Query("DELETE FROM track_table WHERE id = :id")
    suspend fun deleteTracksById(id : String)

}