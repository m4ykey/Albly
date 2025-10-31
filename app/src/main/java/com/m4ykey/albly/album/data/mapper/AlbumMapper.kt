package com.m4ykey.albly.album.data.mapper

import com.m4ykey.albly.album.data.network.AlbumItemDto
import com.m4ykey.albly.album.data.network.AlbumArtistDto
import com.m4ykey.albly.album.data.network.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.ImageDto
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image

fun ImageDto.toDomain() = Image(
    url = url.orEmpty(),
    width = width ?: 0,
    height = height ?: 0
)

fun AlbumItemDto.toDomain() = AlbumItem(
    albumType = album_type.orEmpty(),
    id = id.orEmpty(),
    name = name.orEmpty(),
    releaseDate = release_date.orEmpty(),
    totalTracks = total_tracks ?: 0,
    type = type.orEmpty(),
    images = images?.map { it.toDomain() } ?: emptyList(),
    externalUrls = external_urls?.toDomain() ?: ExternalUrls.EMPTY,
    artists = artists?.map { it.toDomain() } ?: emptyList()
)

fun ExternalUrlsDto.toDomain() = ExternalUrls(
    spotify = spotify.orEmpty()
)

fun AlbumArtistDto.toDomain() = AlbumArtist(
    id = id.orEmpty(),
    name = name.orEmpty(),
    type = type.orEmpty(),
    externalUrls = external_urls?.toDomain() ?: ExternalUrls.EMPTY
)