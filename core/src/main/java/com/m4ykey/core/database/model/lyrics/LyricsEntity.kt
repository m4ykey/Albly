package com.m4ykey.core.database.model.lyrics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lyrics_table")
data class LyricsEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "source") val source : String,
    @ColumnInfo(name = "remote_id") val remoteId : Int,
    @ColumnInfo(name = "isrc") val isrc : String,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "artist") val artist : String,
    @ColumnInfo(name = "album") val album : String,
    @ColumnInfo(name = "lyrics") val lyrics : String,
    @ColumnInfo(name = "save_time") val saveTime : Long? = null,
    @ColumnInfo(name = "image") val image: String
)
