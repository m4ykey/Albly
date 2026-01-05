package com.m4ykey.album.data.local.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ImageEntity(
    @ColumnInfo("height") val height : Int,
    @ColumnInfo("width") val width : Int,
    @ColumnInfo("url") val url : String
)
