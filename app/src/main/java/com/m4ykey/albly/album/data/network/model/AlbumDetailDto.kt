package com.m4ykey.albly.album.data.network.model

data class AlbumDetailDto(
    val album_type: String? = "",
    val artists: List<AlbumArtistDto>? = emptyList(),
    val copyrights: List<CopyrightDto>? = emptyList(),
    val external_urls: ExternalUrlsDto? = null,
    val genres: List<Any?>? = emptyList(),
    val id: String? = "",
    val images: List<ImageDto>? = emptyList(),
    val label: String? = "",
    val name: String? = "",
    val popularity: Int? = 0,
    val release_date: String? = "",
    val total_tracks: Int? = 0,
    val type: String? = ""
)