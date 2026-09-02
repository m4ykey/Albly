package com.m4ykey.search.data.network.dto.album

import kotlinx.serialization.Serializable

@Serializable
data class SearchAlbumRootDto(
    val results : List<ResultsAlbumDto>
)
