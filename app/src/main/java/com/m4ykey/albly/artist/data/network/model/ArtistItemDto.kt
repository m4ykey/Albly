package com.m4ykey.albly.artist.data.network.model

import com.m4ykey.albly.album.data.network.model.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.model.ImageDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistItemDto(
    @param:Json(name = "external_urls") val externalUrls: ExternalUrlsDto? = null,
    val followers: FollowersDto? = null,
    val genres: List<String>? = emptyList(),
    val id: String? = "",
    val images: List<ImageDto>? = emptyList(),
    val name: String? = "",
    val popularity: Int? = 0,
    val type: String? = "",
    val uri: String? = ""
)