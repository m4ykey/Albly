package com.m4ykey.track.data.mapper

import com.m4ykey.core.model.domain.ExternalUrls
import com.m4ykey.core.model.mapper.toAlbumArtistDomain
import com.m4ykey.core.model.mapper.toAlbumArtistEntity
import com.m4ykey.core.model.mapper.toDomain
import com.m4ykey.core.model.mapper.toExternalUrlsDomain
import com.m4ykey.core.model.mapper.toExternalUrlsEntity
import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.data.network.dto.TrackItemDto
import com.m4ykey.track.domain.model.TrackItem

fun TrackEntity.toDomain() = TrackItem(
    id = id,
    previewUrl = previewUrl.orEmpty(),
    discNumber = discNumber,
    durationMs = durationMs,
    name = name,
    trackNumber = trackNumber,
    explicit = explicit,
    artists = artists.map { it.toAlbumArtistDomain() },
    externalUrls = externalUrls.toExternalUrlsDomain()
)

fun TrackItem.toEntity(albumId : String) = TrackEntity(
    id = id,
    name = name,
    trackNumber = trackNumber,
    externalUrls = externalUrls.toExternalUrlsEntity(),
    discNumber = discNumber,
    durationMs = durationMs,
    explicit = explicit,
    previewUrl = previewUrl,
    albumId = albumId,
    artists = artists.map { it.toAlbumArtistEntity() }
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
