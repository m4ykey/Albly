package com.m4ykey.albly.artist.domain.model

import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image

data class ArtistItem(
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val genres : List<String>,
    val id : String,
    val images : List<Image>,
    val name : String,
    val popularity : Int,
    val type : String,
    val uri : String
) {
    companion object {
        val EMPTY_LIST = emptyList<ArtistItem>()
    }
}
