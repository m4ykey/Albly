package com.m4ykey.core.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumsDto(
    val items: List<AlbumItemDto>? = emptyList()
)