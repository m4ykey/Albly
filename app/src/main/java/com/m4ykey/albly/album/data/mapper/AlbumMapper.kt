package com.m4ykey.albly.album.data.mapper

import com.m4ykey.albly.album.data.network.model.AlbumItemDto
import com.m4ykey.albly.album.data.network.model.AlbumsDto
import com.m4ykey.albly.album.data.network.model.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.model.ImageDto
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.Albums
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image
import com.m4ykey.albly.artist.data.mapper.toDomain
import com.m4ykey.albly.artist.domain.model.Artist

fun AlbumItemDto.toDomain() = AlbumItem(
    id = id.orEmpty(),
    name = name.orEmpty(),
    type = type.orEmpty(),
    albumType = albumType.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    uri = uri.orEmpty(),
    totalTracks = totalTracks ?: 0,
    artists = artists?.map { it.toDomain() } ?: Artist.EMPTY,
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY,
    images = images?.map { it.toDomain() } ?: Image.EMPTY_LIST
)

fun AlbumsDto.toDomain() = Albums(
    items = items?.map { it.toDomain() } ?: AlbumItem.EMPTY_LIST
)

fun ImageDto.toDomain() = Image(
    height = height ?: 0,
    width = width ?: 0,
    url = url.orEmpty()
)

fun ExternalUrlsDto.toDomain() = ExternalUrls(
    spotify = spotify.orEmpty()
)