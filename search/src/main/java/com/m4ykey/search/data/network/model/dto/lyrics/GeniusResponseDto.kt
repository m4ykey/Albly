package com.m4ykey.search.data.network.model.dto.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class GeniusResponseDto(
    val hits : List<HitDto>? = emptyList()
)
