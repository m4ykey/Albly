package com.m4ykey.track.domain.model

import androidx.compose.runtime.Immutable
import com.m4ykey.core.model.domain.AlbumArtist
import com.m4ykey.core.model.domain.ExternalUrls

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