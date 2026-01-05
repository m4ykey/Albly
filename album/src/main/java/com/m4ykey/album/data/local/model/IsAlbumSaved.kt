package com.m4ykey.album.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "is_album_saved_table")
data class IsAlbumSaved(
    @ColumnInfo("id") @PrimaryKey(autoGenerate = false) val id : String,
    @ColumnInfo("isAlbumSaved") val isAlbumSaved : Boolean
)