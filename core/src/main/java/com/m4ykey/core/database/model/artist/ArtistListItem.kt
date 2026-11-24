package com.m4ykey.core.database.model.artist

data class ArtistListItem(
    val id : Int,
    val name : String,
    val images : List<ImageEntity>
)
