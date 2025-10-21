package com.m4ykey.albly.artist.domain.model

import com.m4ykey.albly.album.domain.model.ExternalUrls

data class Artist(
    val externalUrls: ExternalUrls,
    val id : String,
    val name : String,
    val type : String,
    val uri : String
) {
    companion object {
        val EMPTY = emptyList<Artist>()
    }
}
