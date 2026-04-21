package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumRootDto(
    val id : Int? = null,
    val main_release : Int? = null,
    val most_recent_release : Int? = null,
    val resource_url : String? = null,
    val versions_url : String? = null,
    val main_release_url : String? = null,
    val most_recent_release_url: String? = null,
    val year: Int? = null,
    val title : String? = null,
    val images : List<ImageDto>? = emptyList(),
    val tracklist : List<TrackListDto>? = emptyList(),
    val artists : List<ArtistsDto>? = emptyList()
)
