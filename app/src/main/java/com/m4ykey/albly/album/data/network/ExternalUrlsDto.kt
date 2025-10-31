package com.m4ykey.albly.album.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExternalUrlsDto(
    val spotify: String? = ""
)