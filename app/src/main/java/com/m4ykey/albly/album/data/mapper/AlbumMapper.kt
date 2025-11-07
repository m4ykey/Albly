package com.m4ykey.albly.album.data.mapper

import com.m4ykey.albly.album.data.network.model.AlbumArtistDto
import com.m4ykey.albly.album.data.network.model.AlbumDetailDto
import com.m4ykey.albly.album.data.network.model.AlbumItemDto
import com.m4ykey.albly.album.data.network.model.CopyrightDto
import com.m4ykey.albly.album.data.network.model.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.model.ImageDto
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.Copyright
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image

fun AlbumDetailDto.toDomain() = AlbumDetail(
    id = id.orEmpty(),
    albumType = album_type.orEmpty(),
    label = label.orEmpty(),
    type = type.orEmpty(),
    name = name.orEmpty(),
    totalTracks = total_tracks ?: 0,
    popularity = popularity ?: 0,
    releaseDate = release_date.orEmpty(),
    images = images?.map { it.toDomain() } ?: emptyList(),
    artists = artists?.map { it.toDomain() } ?: emptyList(),
    genres = genres ?: emptyList(),
    externalUrls = external_urls?.toDomain() ?: ExternalUrls.EMPTY,
    copyright = copyrights?.map { it.toDomain() } ?: emptyList()
)

fun CopyrightDto.toDomain() = Copyright(
    text = text.orEmpty(),
    type = type.orEmpty()
)

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