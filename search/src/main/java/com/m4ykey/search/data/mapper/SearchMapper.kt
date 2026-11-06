package com.m4ykey.search.data.mapper

import com.m4ykey.search.data.network.dto.album.ResultsAlbumDto
import com.m4ykey.search.data.network.dto.artist.ResultsArtistDto
import com.m4ykey.search.data.network.dto.lyrics.HitDto
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.lyrics.Hit

fun HitDto.toHit() : Hit {
    return Hit(
        artist = artist.orEmpty(),
        cover = cover.orEmpty(),
        title = title.orEmpty(),
        isrc = isrc.orEmpty()
    )
}

fun ResultsArtistDto.toResultsArtist() : ResultsArtist {
    return ResultsArtist(
        id = id ?: 0,
        cover_image = coverImage.orEmpty(),
        title = title.orEmpty(),
        thumb = thumb.orEmpty()
    )
}

fun ResultsAlbumDto.toResultsAlbum() : ResultsAlbum {
    val rawTitle = title
    var artist = "Unknown artist"
    var album = rawTitle

    rawTitle?.takeIf { it.contains(" - ") }?.let {
        val parts = it.split(" - ", limit = 2)
        artist = parts[0].trim()
        album = parts[1].trim()
    }

    return ResultsAlbum(
        artist = artist,
        album = album,
        thumb = thumb,
        cover_image = coverImage,
        id = id,
        master_id = masterId
    )
}