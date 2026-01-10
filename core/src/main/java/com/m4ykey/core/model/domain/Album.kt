package com.m4ykey.core.model.domain

data class Album(
    val name : String,
    val artists : List<AlbumArtist>,
    val images : List<Image>,
    val id : String
)
