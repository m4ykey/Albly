package com.m4ykey.albly.track.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.albly.track.data.local.dto.TrackDao
import com.m4ykey.albly.track.data.local.model.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class]
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun trackDao() : TrackDao

}