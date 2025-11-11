package com.m4ykey.albly.album.domain.model

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
