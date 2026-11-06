package com.m4ykey.search.data.network.dto.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class LyricsDto(
    val hits: List<HitDto> = emptyList(),
    val next: String? = null
)