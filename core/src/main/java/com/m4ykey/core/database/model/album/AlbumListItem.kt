package com.m4ykey.core.database.model.album

data class AlbumListItem(
    val id : Int,
    val title : String,
    val image : String,
    val artist : List<ArtistEntity>
)
