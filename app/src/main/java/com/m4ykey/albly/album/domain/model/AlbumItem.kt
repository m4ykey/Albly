package com.m4ykey.albly.album.domain.model

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
