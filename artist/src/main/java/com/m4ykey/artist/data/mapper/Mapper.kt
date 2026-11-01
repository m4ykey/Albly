package com.m4ykey.artist.data.mapper

import com.m4ykey.artist.data.dto.ArtistDto
import com.m4ykey.artist.data.dto.ImageDto
import com.m4ykey.artist.data.dto.ReleaseItemDto
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.Image
import com.m4ykey.artist.domain.model.ReleaseItem

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
        realname = realName.orEmpty(),
        uri = uri.orEmpty(),
        urls = urls,
        images = images.map { it.toImage() }
    )
}

fun ReleaseItemDto.toReleaseItem() : ReleaseItem {
    return ReleaseItem(
        id = id ?: 0,
        resourceUrl = resourceUrl.orEmpty(),
        thumb = thumb.orEmpty(),
        title = title.orEmpty(),
        type = type.orEmpty(),
        year = year ?: 0
    )
}