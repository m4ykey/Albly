package com.m4ykey.search.data.network.model.dto.artist

import kotlinx.serialization.Serializable

@Serializable
data class SearchArtistRootDto(
    val results : List<ResultsArtistDto>? = emptyList()
)

