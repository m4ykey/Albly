package com.m4ykey.album.data.network.dto.new_release

import kotlinx.serialization.Serializable

@Serializable
data class NewReleaseRootDto(
    val results: List<NewReleaseResultDto>? = emptyList()
)