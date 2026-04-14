package com.m4ykey.album.domain.model

data class AlbumRoot(
    val id : Int?,
    val year: Int?,
    val title : String?,
    val images : List<Image>?,
    val trackList : List<TrackList>?,
    val artists : List<Artists>?
)
