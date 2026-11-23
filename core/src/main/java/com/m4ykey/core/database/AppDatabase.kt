package com.m4ykey.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m4ykey.core.database.converter.AlbumConverters
import com.m4ykey.core.database.dao.AlbumDao
import com.m4ykey.core.database.dao.ArtistReleaseDao
import com.m4ykey.core.database.dao.LyricsDao
import com.m4ykey.core.database.dao.NewReleaseDao
import com.m4ykey.core.database.dao.SearchDao
import com.m4ykey.core.database.model.album.AlbumEntity
import com.m4ykey.core.database.model.album.IsAlbumSaved
import com.m4ykey.core.database.model.album.IsListenLaterSaved
import com.m4ykey.core.database.model.album.NewReleaseCursor
import com.m4ykey.core.database.model.album.NewReleaseEntity
import com.m4ykey.core.database.model.artist.ArtistReleaseCursor
import com.m4ykey.core.database.model.artist.ArtistReleaseEntity
import com.m4ykey.core.database.model.lyrics.IsLyricsSaved
import com.m4ykey.core.database.model.lyrics.LyricsEntity
import com.m4ykey.core.database.model.search.SearchItem

@Database(
    entities = [
        AlbumEntity::class,
        IsAlbumSaved::class,
        IsListenLaterSaved::class,
        LyricsEntity::class,
        IsLyricsSaved::class,
        SearchItem::class,
        NewReleaseEntity::class,
        NewReleaseCursor::class,
        ArtistReleaseEntity::class,
        ArtistReleaseCursor::class
    ],
    version = 9
)
@TypeConverters(AlbumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao
    abstract fun lyricsDao() : LyricsDao
    abstract fun searchDao() : SearchDao
    abstract fun newReleaseDao() : NewReleaseDao
    abstract fun artistReleaseDao() : ArtistReleaseDao

}