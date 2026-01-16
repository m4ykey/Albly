package com.m4ykey.core.model.domain

data class ArtistItem(
    val externalUrls: ExternalUrls,
    val followers : Followers,
    val id : String,
    val images : List<Image>,
    val name : String,
    val popularity : Int
)