package com.m4ykey.core.database.model.lyrics

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "is_lyrics_saved_table",
    primaryKeys = ["source", "remote_id"]
)
data class IsLyricsSaved(
    @ColumnInfo("isLyricsSaved") val isLyricsSaved : Boolean,
    @ColumnInfo("remote_id") val remoteId : Int = 0,
    @ColumnInfo("isrc") val isrc : String? = null,
    @ColumnInfo("source") val source : String
)
