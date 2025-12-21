package com.m4ykey.albly.album.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4ykey.albly.album.data.local.dao.AlbumDao
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.AlbumWithStates
import com.m4ykey.albly.album.data.local.model.IsAlbumSaved
import com.m4ykey.albly.album.data.local.model.IsListenLaterSaved

@Database(
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        AlbumWithStates::class
               ],
    version = 1
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}