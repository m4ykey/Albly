package com.m4ykey.albly.artist.data.network.model

import com.m4ykey.albly.album.data.network.model.ExternalUrlsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistDto(
    @param:Json(name = "external_urls") val externalUrls: ExternalUrlsDto? = null,
    val id: String? = "",
    val name: String? = "",
    val type: String? = "",
    val uri: String? = ""
)