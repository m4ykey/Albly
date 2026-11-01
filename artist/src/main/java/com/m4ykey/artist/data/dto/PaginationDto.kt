package com.m4ykey.artist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val page : Int? = null,
    val pages : Int? = null,
    @SerialName("per_page") val perPage : Int? = null,
    val items : Int? = null
)
