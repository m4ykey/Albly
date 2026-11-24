package com.m4ykey.artist.data.mapper

import com.m4ykey.artist.data.network.dto.ArtistDto
import com.m4ykey.artist.data.network.dto.ImageDto
import com.m4ykey.artist.data.network.dto.ReleaseItemDto
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.Image
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.core.database.model.artist.ArtistReleaseEntity

fun ReleaseItemDto.toEntity(
    fetchedAt : Long,
    artistId : Int,
    position : Int
) : ArtistReleaseEntity {
    return ArtistReleaseEntity(
        thumb = thumb.orEmpty(),
        fetchedAt = fetchedAt,
        releaseId = id ?: 0,
        title = title.orEmpty(),
        year = year ?: 0,
        releaseType = type.orEmpty().ifBlank { "release" },
        resourceUrl = resourceUrl.orEmpty(),
        artistId = artistId,
        position = position
    )
}

fun ArtistReleaseEntity.toReleaseItem() : ReleaseItem {
    return ReleaseItem(
        id = releaseId,
        title = title,
        thumb = thumb,
        year = year,
        type = releaseType,
        resourceUrl = resourceUrl
    )
}

fun ImageDto.toImage() : Image {
    return Image(
        width = width ?: 0,
        height = height ?: 0,
        type = type.orEmpty(),
        uri = uri.orEmpty()
    )
}

fun ArtistDto.toArtist() : Artist {
    return Artist(
        id = id ?: 0,
        name = name.orEmpty(),
        profile = profile.orEmpty(),
        realname = realName.orEmpty(),
        uri = uri.orEmpty(),
        urls = urls,
        images = images.map { it.toImage() }
    )
}

fun ReleaseItemDto.toReleaseItem() : ReleaseItem {
    return ReleaseItem(
        id = id ?: 0,
        resourceUrl = resourceUrl.orEmpty(),
        thumb = thumb.orEmpty(),
        title = title.orEmpty(),
        type = type.orEmpty(),
        year = year ?: 0
    )
}