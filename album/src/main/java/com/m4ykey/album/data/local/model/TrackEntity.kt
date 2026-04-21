package com.m4ykey.album.data.local.model

import androidx.room.ColumnInfo

data class TrackEntity(
    @ColumnInfo("position") val position : String,
    @ColumnInfo("title") val title : String,
    @ColumnInfo("duration") val duration : String,
    @ColumnInfo("track_id") val track_id : Int
)
