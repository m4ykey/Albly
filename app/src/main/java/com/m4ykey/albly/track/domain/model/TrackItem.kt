package com.m4ykey.albly.track.domain.model

import androidx.compose.runtime.Immutable
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.ExternalUrls

@Immutable
data class TrackItem(
    val artists : List<AlbumArtist>,
    val discNumber : Int,
    val durationMs : Int,
    val explicit : Boolean,
    val externalUrls: ExternalUrls,
    val id : String,
    val name : String,
    val previewUrl : String,
    val trackNumber : Int
)