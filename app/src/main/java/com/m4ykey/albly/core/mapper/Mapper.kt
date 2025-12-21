package com.m4ykey.albly.core.mapper

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.ArtistEntity
import com.m4ykey.albly.album.data.local.model.CopyrightEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.albly.album.data.local.model.ImageEntity
import com.m4ykey.albly.album.data.network.dto.AlbumArtistDto
import com.m4ykey.albly.album.data.network.dto.AlbumDetailDto
import com.m4ykey.albly.album.data.network.dto.AlbumItemDto
import com.m4ykey.albly.album.data.network.dto.CopyrightDto
import com.m4ykey.albly.album.data.network.dto.ExternalUrlsDto
import com.m4ykey.albly.album.data.network.dto.ImageDto
import com.m4ykey.albly.album.domain.model.AlbumArtist
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.Copyright
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image
import com.m4ykey.albly.track.data.network.dto.TrackItemDto
import com.m4ykey.albly.track.domain.model.TrackItem

fun ExternalUrls.toEntity() = ExternalUrlsEntity(
    spotify = spotify
)

fun AlbumArtist.toEntity() = ArtistEntity(
    name = name,
    id = id,
    externalUrls = externalUrls.toEntity()
)

fun Copyright.toEntity() = CopyrightEntity(
    text = text
)

fun Image.toEntity() = ImageEntity(
    height = height,
    url = url,
    width = width
)

fun AlbumDetail.toEntity() = AlbumEntity(
    id = id,
    label = label,
    name = name,
    releaseDate = releaseDate,
    totalTracks = totalTracks,
    albumType = albumType,
    artists = artists.map { it.toEntity() },
    images = images.map { it.toEntity() },
    externalUrls = externalUrls.toEntity(),
    copyrights = copyright.map { it.toEntity() }
)

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