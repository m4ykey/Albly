package com.m4ykey.search.data.network.dto.album

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultsAlbumDto(
    val title : String? = null,
    val thumb : String? = null,
    @SerialName("cover_image") val coverImage : String? = null,
    val id : Int? = null,
    @SerialName("master_id") val masterId : Int? = null
)
