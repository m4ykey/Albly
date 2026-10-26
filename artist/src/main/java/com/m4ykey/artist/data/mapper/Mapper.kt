package com.m4ykey.artist.data.mapper

import com.m4ykey.artist.data.dto.ArtistDto
import com.m4ykey.artist.data.dto.ImageDto
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.Image

fun ImageDto.toImage() : Image {
    return Image(
        width = width ?: 0,
        height = height ?: 0,
        type = type.orEmpty(),
        uri = uri.orEmpty()
    )
}

fun ArtistDto.toArtist() : Artist {
    return Artist(
        id = id ?: 0,
        name = name.orEmpty(),
        profile = profile.orEmpty(),
        realname = realname.orEmpty(),
        uri = uri.orEmpty(),
        urls = urls,
        images = images.map { it.toImage() }
    )
}