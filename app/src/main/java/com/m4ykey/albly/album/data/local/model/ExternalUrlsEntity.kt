package com.m4ykey.albly.album.data.local.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrlsEntity(
    @ColumnInfo("spotify") val spotify : String
)
