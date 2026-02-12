package com.m4ykey.album.data.mapper

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.CopyrightEntity
import com.m4ykey.album.data.local.model.ImageEntity
import com.m4ykey.core.model.mapper.toAlbumArtistDomain
import com.m4ykey.core.model.mapper.toAlbumArtistEntity
import com.m4ykey.core.model.mapper.toDomain
import com.m4ykey.core.model.mapper.toExternalUrlsDomain
import com.m4ykey.core.model.mapper.toExternalUrlsEntity

fun AlbumEntity.toAlbum() = Album(
    name = name,
    id = id,
    images = images.map { it.toDomain() },
    artists = artists.map { it.toAlbumArtistDomain() }
)

fun Copyright.toEntity() = CopyrightEntity(
    text = text
)

fun Image.toEntity() = ImageEntity(
    height = height,
    url = url,
    width = width
)

fun ImageEntity.toDomain() = Image(
    height = height,
    url = url,
    width = width
)

fun CopyrightEntity.toDomain() = Copyright(
    text = text,
    type = ""
)

fun AlbumEntity.toDomain() = AlbumDetail(
    id = id,
    name = name,
    albumType = albumType,
    totalTracks = totalTracks,
    releaseDate = releaseDate,
    type = albumType,
    images = images.map { it.toDomain() },
    externalUrls = externalUrls.toExternalUrlsDomain(),
    artists = artists.map { it.toAlbumArtistDomain() },
    label = label,
    popularity = 0,
    copyright = copyrights.map { it.toDomain() },
    genres = emptyList()
)

fun AlbumDetail.toEntity() = AlbumEntity(
    id = id,
    label = label,
    name = name,
    releaseDate = releaseDate,
    totalTracks = totalTracks,
    albumType = albumType,
    artists = artists.map { it.toAlbumArtistEntity() },
    images = images.map { it.toEntity() },
    externalUrls = externalUrls.toExternalUrlsEntity(),
    copyrights = copyright.map { it.toEntity() }
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


