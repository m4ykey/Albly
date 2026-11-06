package com.m4ykey.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.core.database.converter.AlbumConverters
import com.m4ykey.core.database.dao.AlbumDao
import com.m4ykey.core.database.dao.LyricsDao
import com.m4ykey.core.database.model.album.AlbumEntity
import com.m4ykey.core.database.model.album.IsAlbumSaved
import com.m4ykey.core.database.model.album.IsListenLaterSaved
import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity

@Database(
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        LyricsEntity::class,
        IsLyricsSaved::class
    ],
    version = 6
)
@TypeConverters(AlbumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun lyricsDao() : LyricsDao

}