package com.m4ykey.albly.album.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "is_listen_later_saved_table")
data class IsListenLaterSaved(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id : String,
    @ColumnInfo("isListenLater") val isListenLaterSaved : Boolean
)