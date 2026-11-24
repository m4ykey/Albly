package com.m4ykey.core.database.model.artist

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ImageEntity(
    @ColumnInfo(name = "width") val width : Int,
    @ColumnInfo(name = "height") val height : Int,
    @ColumnInfo(name = "uri") val uri : String,
    @ColumnInfo(name = "type") val type : String
)
