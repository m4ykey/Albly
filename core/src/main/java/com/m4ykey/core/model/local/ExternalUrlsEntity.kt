package com.m4ykey.core.model.local

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrlsEntity(
    @ColumnInfo("spotify") val spotify : String
)
