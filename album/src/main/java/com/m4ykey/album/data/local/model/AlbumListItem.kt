package com.m4ykey.album.data.local.model

data class AlbumListItem(
    val id : Int,
    val title : String,
    val image : String,
    val artist : List<ArtistEntity>
)
