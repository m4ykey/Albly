package com.m4ykey.albly.album.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CopyrightDto(
    val text: String? = "",
    val type: String? = ""
)