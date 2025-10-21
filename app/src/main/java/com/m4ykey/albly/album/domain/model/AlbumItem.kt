package com.m4ykey.albly.album.domain.model

import com.m4ykey.albly.artist.domain.model.Artist

data class AlbumItem(
    val albumType : String,
    val artists : List<Artist>,
    val externalUrls: ExternalUrls,
    val id : String,
    val images : List<Image>,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int,
    val type : String,
    val uri : String
) {
    companion object {
        val EMPTY_LIST = emptyList<AlbumItem>()
    }
}
