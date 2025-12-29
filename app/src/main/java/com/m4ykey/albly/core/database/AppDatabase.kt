package com.m4ykey.albly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.albly.album.data.local.converter.AlbumConverters
import com.m4ykey.albly.album.data.local.dao.AlbumDao
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.IsAlbumSaved
import com.m4ykey.albly.album.data.local.model.IsListenLaterSaved
import com.m4ykey.albly.track.data.local.dao.TrackDao
import com.m4ykey.albly.track.data.local.model.TrackEntity

@Database(
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        TrackEntity::class
    ],
    version = 1
)
@TypeConverters(AlbumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun trackDao() : TrackDao

}