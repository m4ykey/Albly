package com.m4ykey.core.model.mapper

import com.m4ykey.core.model.domain.AlbumArtist
import com.m4ykey.core.model.domain.ExternalUrls
import com.m4ykey.core.model.dto.AlbumArtistDto
import com.m4ykey.core.model.dto.ExternalUrlsDto
import com.m4ykey.core.model.local.ArtistEntity
import com.m4ykey.core.model.local.ExternalUrlsEntity

fun ExternalUrls.toExternalUrlsEntity() = ExternalUrlsEntity(
    spotify = spotify
)

fun AlbumArtist.toAlbumArtistEntity() = ArtistEntity(
    name = name,
    id = id,
    externalUrls = externalUrls.toExternalUrlsEntity()
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

fun ExternalUrlsEntity.toExternalUrlsDomain() = ExternalUrls(
    spotify = spotify
)

fun ArtistEntity.toAlbumArtistDomain() = AlbumArtist(
    id = id,
    name = name,
    externalUrls = externalUrls.toExternalUrlsDomain(),
    type = ""
)