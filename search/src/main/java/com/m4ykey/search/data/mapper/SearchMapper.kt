package com.m4ykey.search.data.mapper

import com.m4ykey.search.data.network.dto.album.ResultsAlbumDto
import com.m4ykey.search.data.network.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.dto.artist.ResultsArtistDto
import com.m4ykey.search.data.network.dto.artist.SearchArtistRootDto
import com.m4ykey.search.data.network.dto.lyrics.GeniusResponseDto
import com.m4ykey.search.data.network.dto.lyrics.GeniusResultDto
import com.m4ykey.search.data.network.dto.lyrics.GeniusRootDto
import com.m4ykey.search.data.network.dto.lyrics.HitDto
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.album.SearchAlbumRoot
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.artist.SearchArtistRoot
import com.m4ykey.search.domain.model.lyrics.GeniusResponse
import com.m4ykey.search.domain.model.lyrics.GeniusResult
import com.m4ykey.search.domain.model.lyrics.GeniusRoot
import com.m4ykey.search.domain.model.lyrics.Hit

fun GeniusRootDto.toGeniusRoot() : GeniusRoot {
    return GeniusRoot(
        response = response.toGeniusResponse()
    )
}

fun GeniusResponseDto.toGeniusResponse() : GeniusResponse {
    return GeniusResponse(
        hits = hits?.map { it.toHit() } ?: emptyList()
    )
}

fun HitDto.toHit() : Hit {
    return Hit(
        result = result.toGeniusResult()
    )
}

fun GeniusResultDto.toGeniusResult() : GeniusResult {
    return GeniusResult(
        title = title.orEmpty(),
        artistNames = artistNames.orEmpty(),
        songArtImageUrl = songArtImageUrl.orEmpty()
    )
}

fun SearchArtistRootDto.toSearchArtistRoot() : SearchArtistRoot {
    return SearchArtistRoot(
        results = results.map { it.toResultsArtist() }
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