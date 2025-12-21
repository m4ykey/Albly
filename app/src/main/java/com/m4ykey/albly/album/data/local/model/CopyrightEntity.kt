package com.m4ykey.albly.album.data.local.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class CopyrightEntity(
    @ColumnInfo("text") val text: String
)
