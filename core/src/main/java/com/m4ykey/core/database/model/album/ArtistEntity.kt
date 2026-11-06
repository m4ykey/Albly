package com.m4ykey.core.database.model.album

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ArtistEntity(
    @ColumnInfo("artist_id") val artist_id : Int,
    @ColumnInfo("name") val name : String
)
