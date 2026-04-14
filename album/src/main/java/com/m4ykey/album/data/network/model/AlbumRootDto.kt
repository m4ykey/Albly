package com.m4ykey.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumRootDto(
    val id : Int?,
    val main_release : Int?,
    val most_recent_release : Int?,
    val resource_url : String?,
    val versions_url : String?,
    val main_release_url : String?,
    val most_recent_release_url: String?,
    val year: Int?,
    val title : String?,
    val images : List<ImageDto>?,
    val trackList : List<TrackListDto>?,
    val artists : List<ArtistsDto>?
)
