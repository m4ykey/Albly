package com.m4ykey.album.data.local.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ArtistEntity(
    @ColumnInfo("artist_id") val artist_id : Int,
    @ColumnInfo("name") val name : String
)
