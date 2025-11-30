package com.m4ykey.albly.album.data.mapper

import com.m4ykey.albly.album.data.network.dto.AlbumArtistDto
import com.m4ykey.albly.album.data.network.dto.AlbumDetailDto
import com.m4ykey.albly.album.data.network.dto.AlbumItemDto
import com.m4ykey.albly.album.data.network.dto.CopyrightDto
import com.m4ykey.albly.album.data.network.dto.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.dto.ImageDto
import com.m4ykey.albly.album.data.network.dto.TrackItemDto
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.Copyright
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image
import com.m4ykey.albly.album.domain.model.TrackItem

fun TrackItemDto.toDomain() = TrackItem(
    id = id.orEmpty(),
    name = name.orEmpty(),
    discNumber = discNumber ?: 0,
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY,
    previewUrl = previewUrl.orEmpty(),
    durationMs = durationMs ?: 0,
    explicit = explicit ?: false,
    trackNumber = trackNumber ?: 0,
    artists = artists?.map { it.toDomain() } ?: emptyList()
)

fun AlbumDetailDto.toDomain() = AlbumDetail(
    id = id.orEmpty(),
    albumType = albumType.orEmpty(),
    label = label.orEmpty(),
    type = type.orEmpty(),
    name = name.orEmpty(),
    totalTracks = totalTracks ?: 0,
    popularity = popularity ?: 0,
    releaseDate = releaseDate.orEmpty(),
    images = images?.map { it.toDomain() } ?: emptyList(),
    artists = artists?.map { it.toDomain() } ?: emptyList(),
    genres = genres ?: emptyList(),
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY,
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
    albumType = albumType.orEmpty(),
    id = id.orEmpty(),
    name = name.orEmpty(),
    releaseDate = releaseDate.orEmpty(),
    totalTracks = totalTracks ?: 0,
    type = type.orEmpty(),
    images = images?.map { it.toDomain() } ?: emptyList(),
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY,
    artists = artists?.map { it.toDomain() } ?: emptyList()
)

fun ExternalUrlsDto.toDomain() = ExternalUrls(
    spotify = spotify.orEmpty()
)

fun AlbumArtistDto.toDomain() = AlbumArtist(
    id = id.orEmpty(),
    name = name.orEmpty(),
    type = type.orEmpty(),
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY
)