package com.m4ykey.core.model.mapper

import com.m4ykey.core.model.domain.AlbumArtist
import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.core.model.domain.ArtistItem
import com.m4ykey.core.model.domain.ExternalUrls
import com.m4ykey.core.model.domain.Followers
import com.m4ykey.core.model.domain.Image
import com.m4ykey.core.model.dto.AlbumArtistDto
import com.m4ykey.core.model.dto.AlbumItemDto
import com.m4ykey.core.model.dto.ArtistItemDto
import com.m4ykey.core.model.dto.ExternalUrlsDto
import com.m4ykey.core.model.dto.FollowersDto
import com.m4ykey.core.model.dto.ImageDto
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

fun ImageDto.toDomain() = Image(
    url = url.orEmpty(),
    width = width ?: 0,
    height = height ?: 0
)

fun FollowersDto.toDomain() = Followers(
    total = total ?: 0
)

fun ArtistItemDto.toDomain() = ArtistItem(
    id = id.orEmpty(),
    name = name.orEmpty(),
    externalUrls = external_urls?.toDomain() ?: ExternalUrls.EMPTY,
    images = images?.map { it.toDomain() } ?: emptyList(),
    popularity = popularity ?: 0,
    followers = followers?.toDomain() ?: Followers.EMPTY
)