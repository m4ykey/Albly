package com.m4ykey.album.domain.model

import androidx.compose.runtime.Immutable
import com.m4ykey.core.model.domain.AlbumArtist
import com.m4ykey.core.model.domain.ExternalUrls
import com.m4ykey.core.model.domain.Image

@Immutable
data class AlbumDetail(
    val albumType : String,
    val artists : List<AlbumArtist>,
    val copyright : List<Copyright>,
    val externalUrls: ExternalUrls,
    val genres : List<Any?>,
    val id : String,
    val images : List<Image>,
    val label : String,
    val name : String,
    val popularity : Int,
    val releaseDate : String,
    val totalTracks : Int,
    val type : String
)
