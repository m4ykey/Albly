package com.m4ykey.albly.album.data.local.model

import androidx.room.ColumnInfo

data class ImageEntity(
    @ColumnInfo("height") val height : Int,
    @ColumnInfo("width") val width : Int,
    @ColumnInfo("url") val url : String
)
