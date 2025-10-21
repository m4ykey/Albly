package com.m4ykey.albly.artist.data.mapper

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.domain.model.ExternalUrls
import com.m4ykey.albly.album.domain.model.Image
import com.m4ykey.albly.artist.data.network.model.ArtistDto
import com.m4ykey.albly.artist.data.network.model.ArtistItemDto
import com.m4ykey.albly.artist.data.network.model.ArtistsDto
import com.m4ykey.albly.artist.data.network.model.FollowersDto
import com.m4ykey.albly.artist.domain.model.Artist
import com.m4ykey.albly.artist.domain.model.ArtistItem
import com.m4ykey.albly.artist.domain.model.Artists
import com.m4ykey.albly.artist.domain.model.Followers

fun FollowersDto.toDomain() = Followers(
    total = total ?: 0
)

fun ArtistDto.toDomain() = Artist(
    id = id.orEmpty(),
    uri = uri.orEmpty(),
    name = name.orEmpty(),
    type = type.orEmpty(),
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY
)

fun ArtistsDto.toDomain() = Artists(
    items = items?.map { it.toDomain() } ?: ArtistItem.EMPTY_LIST
)

fun ArtistItemDto.toDomain() = ArtistItem(
    popularity = popularity ?: 0,
    id = id.orEmpty(),
    followers = followers?.toDomain() ?: Followers.EMPTY,
    images = images?.map { it.toDomain() } ?: Image.EMPTY_LIST,
    name = name.orEmpty(),
    type = type.orEmpty(),
    uri = uri.orEmpty(),
    externalUrls = externalUrls?.toDomain() ?: ExternalUrls.EMPTY,
    genres = genres.orEmpty()
)