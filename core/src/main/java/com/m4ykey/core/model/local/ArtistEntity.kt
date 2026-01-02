package com.m4ykey.core.model.local

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ArtistEntity(
    @ColumnInfo("name") val name : String,
    @ColumnInfo("id") val id : String,
    @ColumnInfo("external_urls") val externalUrls : ExternalUrlsEntity
)
