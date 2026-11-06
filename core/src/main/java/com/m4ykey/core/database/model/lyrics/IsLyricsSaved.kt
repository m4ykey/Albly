package com.m4ykey.core.database.model.lyrics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "is_lyrics_saved_table")
data class IsLyricsSaved(
    @ColumnInfo("isLyricsSaved") val isLyricsSaved : Boolean,
    @ColumnInfo("track_key") @PrimaryKey(autoGenerate = false) val trackKey : String
)
