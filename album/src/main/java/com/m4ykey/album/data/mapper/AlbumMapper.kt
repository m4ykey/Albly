package com.m4ykey.album.data.mapper

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.ArtistEntity
import com.m4ykey.album.data.local.model.TrackEntity
import com.m4ykey.album.data.network.dto.detail.AlbumRootDto
import com.m4ykey.album.data.network.dto.detail.ArtistsDto
import com.m4ykey.album.data.network.dto.detail.ImageDto
import com.m4ykey.album.data.network.dto.detail.TrackListDto
import com.m4ykey.album.data.network.dto.new_release.NewReleaseResultDto
import com.m4ykey.album.data.network.dto.new_release.NewReleaseRootDto
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.model.detail.Artists
import com.m4ykey.album.domain.model.detail.Image
import com.m4ykey.album.domain.model.detail.TrackList
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.model.new_release.NewReleaseRoot

fun NewReleaseRootDto.toNewReleaseRoot() : NewReleaseRoot {
    return NewReleaseRoot(
        results = results.map { it.toNewReleaseResult() }
    )
}

fun NewReleaseResultDto.toNewReleaseResult() : NewReleaseResult {
    val rawTitle = title
    var artist = "Unknown artist"
    var album = rawTitle

    rawTitle?.takeIf { it.contains(" - ") }?.let {
        val parts = it.split(" - ", limit = 2)
        artist = parts[0].trim()
        album = parts[1].trim()
    }

    return NewReleaseResult(
        coverImage = coverImage.orEmpty(),
        masterId = masterId ?: 0,
        thumb = thumb.orEmpty(),
        title = album.orEmpty(),
        artist = artist
    )
}

fun AlbumRoot.toAlbumEntity() : AlbumEntity {
    return AlbumEntity(
        id = id,
        title = title,
        year = year,
        save_time = System.currentTimeMillis(),
        image = images.firstOrNull()?.uri.orEmpty(),
        uri = uri,
        trackList = tracklist.map { it.toTrackEntity() },
        artistList = artists.map { it.toArtistEntity() }
    )
}

fun Artists.toArtistEntity() : ArtistEntity {
    return ArtistEntity(
        name = name,
        artist_id = 0
    )
}

fun TrackList.toTrackEntity() : TrackEntity {
    return TrackEntity(
        position = position,
        duration = duration,
        track_id = 0,
        title = title
    )
}

fun AlbumEntity.toAlbumRoot() : AlbumRoot {
    return AlbumRoot(
        id = id,
        title = title,
        year = year,
        uri = uri,
        tracklist = trackList.map { it.toTrackList() },
        artists = artistList.map { it.toArtists() },
        images = image.toImageList()
    )
}

fun ArtistEntity.toArtists() : Artists {
    return Artists(
        name = name,
        id = artist_id
    )
}

fun TrackEntity.toTrackList() : TrackList {
    return TrackList(
        duration = duration,
        type = "",
        position = position,
        title = title
    )
}

fun String?.toImageList() : List<Image> {
    if (isNullOrEmpty()) return emptyList()

    return listOf(
        Image(
            uri = this,
            type = "primary"
        )
    )
}

fun AlbumRootDto.toAlbumRoot() : AlbumRoot {
    return AlbumRoot(
        id = id ?: 0,
        title = title.orEmpty(),
        uri = uri.orEmpty(),
        year = year ?: 0,
        tracklist = tracklist?.map { it.toTrackList() } ?: emptyList(),
        artists = artists?.map { it.toArtists() } ?: emptyList(),
        images = images?.map { it.toImage() } ?: emptyList()
    )
}

fun ImageDto.toImage() : Image {
    return Image(
        type = type.orEmpty(),
        uri = uri.orEmpty()
    )
}

fun TrackListDto.toTrackList() : TrackList {
    return TrackList(
        duration = duration.orEmpty(),
        position = position.orEmpty(),
        type = type.orEmpty(),
        title = title.orEmpty()
    )
}

fun ArtistsDto.toArtists() : Artists {
    return Artists(
        name = name.orEmpty(),
        id = id ?: 0
    )
}