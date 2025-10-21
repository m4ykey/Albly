package com.m4ykey.albly.artist.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FollowersDto(
    val total: Int? = 0
)