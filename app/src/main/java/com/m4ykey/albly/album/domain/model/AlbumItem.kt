package com.m4ykey.albly.album.domain.model

import androidx.compose.runtime.Immutable
import com.m4ykey.core.model.domain.AlbumArtist
import com.m4ykey.core.model.domain.ExternalUrls

@Immutable
data class AlbumItem(
    val albumType : String,
    val artists : List<AlbumArtist>,
    val externalUrls : ExternalUrls,
    val id : String,
    val images : List<Image>,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int,
    val type : String
)
