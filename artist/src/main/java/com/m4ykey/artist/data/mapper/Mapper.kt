package com.m4ykey.artist.data.mapper

import com.m4ykey.artist.data.network.dto.ArtistDto
import com.m4ykey.artist.data.network.dto.ImageDto
import com.m4ykey.artist.data.network.dto.ReleaseItemDto
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.Image
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.core.database.model.artist.ArtistEntity
import com.m4ykey.core.database.model.artist.ArtistReleaseEntity
import com.m4ykey.core.database.model.artist.ImageEntity

fun ArtistEntity.toArtist() : Artist {
    return Artist(
        name = name,
        realname = realname,
        id = id,
        uri = uri,
        urls = urls,
        profile = profile,
        images = images.map { it.toImage() }
    )
}

fun ImageEntity.toImage() : Image {
    return Image(
        width = width,
        height = height,
        type = type,
        uri = uri
    )
}

fun Artist.toArtistEntity(
    saveTime : Long = System.currentTimeMillis()
) : ArtistEntity {
    return ArtistEntity(
        realname = realname,
        id = id,
        name = name,
        profile = profile,
        uri = uri,
        urls = urls.map { it },
        images = images.map { it.toImage() },
        save_time = saveTime
    )
}

fun Image.toImage() : ImageEntity {
    return ImageEntity(
        height = height,
        width = width,
        type = type,
        uri = uri
    )
}

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