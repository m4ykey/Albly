package com.m4ykey.search.data.network.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResultsAlbumDto(
    val title : String?,
    val thumb : String?,
    val cover_image : String?,
    val id : Int?,
    val master_id : Int?
)
