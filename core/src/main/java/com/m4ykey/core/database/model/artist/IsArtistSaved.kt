package com.m4ykey.core.database.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "is_artist_saved_table")
data class IsArtistSaved(
    @ColumnInfo("id") @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo("isArtistSaved") val isArtistSaved : Boolean
)
