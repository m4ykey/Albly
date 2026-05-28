package com.m4ykey.search.data.network.model.dto.album

import kotlinx.serialization.Serializable

@Serializable
data class SearchAlbumRootDto(
    val results : List<ResultsAlbumDto>
)
