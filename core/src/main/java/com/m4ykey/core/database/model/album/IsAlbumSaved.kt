package com.m4ykey.core.database.model.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "is_album_saved_table")
data class IsAlbumSaved(
    @ColumnInfo("id") @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo("isAlbumSaved") val isAlbumSaved : Boolean
)