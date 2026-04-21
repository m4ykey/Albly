package com.m4ykey.album.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class AlbumEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id") val id : Int,
    @ColumnInfo("title") val title : String,
    @ColumnInfo("year") val year : Int,
    @ColumnInfo("trackList") val trackList : List<TrackEntity>,
    @ColumnInfo("artist") val artistList : List<ArtistEntity>,
    @ColumnInfo("image") val image : String,
    @ColumnInfo("save_time") val save_time : Long? = null
)
