package com.m4ykey.album.data.network.dto

import com.m4ykey.core.model.dto.AlbumArtistDto
import com.m4ykey.core.model.dto.ExternalUrlsDto
import com.m4ykey.core.model.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetailDto(
    @SerialName("album_type") val albumType: String? = null,
    val artists: List<AlbumArtistDto>? = emptyList(),
    val copyrights: List<CopyrightDto>? = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val genres: List<String>? = emptyList(),
    val id: String? = null,
    val images: List<ImageDto>? = emptyList(),
    val label: String? = null,
    val name: String? = null,
    val popularity: Int? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("total_tracks") val totalTracks: Int? = null,
    val type: String? = null
)