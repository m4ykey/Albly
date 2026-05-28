package com.m4ykey.search.data.mapper

import com.m4ykey.search.data.network.model.dto.album.ResultsAlbumDto
import com.m4ykey.search.data.network.model.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.model.dto.artist.ResultsArtistDto
import com.m4ykey.search.data.network.model.dto.artist.SearchArtistRootDto
import com.m4ykey.search.data.network.model.dto.lyrics.GeniusResponseDto
import com.m4ykey.search.data.network.model.dto.lyrics.GeniusResultDto
import com.m4ykey.search.data.network.model.dto.lyrics.GeniusRootDto
import com.m4ykey.search.data.network.model.dto.lyrics.HitDto
import com.m4ykey.search.domain.model.search.album.ResultsAlbum
import com.m4ykey.search.domain.model.search.album.SearchAlbumRoot
import com.m4ykey.search.domain.model.search.artist.ResultsArtist
import com.m4ykey.search.domain.model.search.artist.SearchArtistRoot
import com.m4ykey.search.domain.model.search.lyrics.GeniusResponse
import com.m4ykey.search.domain.model.search.lyrics.GeniusResult
import com.m4ykey.search.domain.model.search.lyrics.GeniusRoot
import com.m4ykey.search.domain.model.search.lyrics.Hit

object SearchMapper {

    fun mapToGeniusRootDomain(dto : GeniusRootDto) : GeniusRoot {
        return GeniusRoot(
            response = getGeniusResponse(dto.response)
        )
    }

    private fun getGeniusResponse(dto : GeniusResponseDto) : GeniusResponse {
        return GeniusResponse(
            hits = getHitsList(dto.hits ?: emptyList())
        )
    }

    private fun getHitsList(dto : List<HitDto>) : List<Hit> {
        if (dto.isEmpty()) return emptyList()

        return dto.map { hitDto ->
            Hit(
                result = mapToGeniusResult(hitDto.result)
            )
        }
    }

    private fun mapToGeniusResult(dto : GeniusResultDto) : GeniusResult {
        return GeniusResult(
            title = dto.title.orEmpty(),
            artistNames = dto.artist_names.orEmpty(),
            songArtImageUrl = dto.song_art_image_url.orEmpty()
        )
    }

    fun mapToArtistDomain(dto: SearchArtistRootDto?) : SearchArtistRoot {
        val results = dto?.results?.mapNotNull { mapToSingleArtistResult(it) } ?: emptyList()
        return SearchArtistRoot(results)
    }

    fun mapToSingleArtistResult(dto : ResultsArtistDto?) : ResultsArtist? {
        if (dto == null) return null

        return ResultsArtist(
            cover_image = dto.cover_image.orEmpty(),
            id = dto.id ?: 0,
            thumb = dto.thumb.orEmpty(),
            title = dto.title.orEmpty()
        )
    }

    fun mapToDomain(dto : SearchAlbumRootDto?) : SearchAlbumRoot {
        val results = dto?.results?.mapNotNull { mapToSingleResult(it) } ?: emptyList()
        return SearchAlbumRoot(results)
    }

    fun mapToSingleResult(dto : ResultsAlbumDto?) : ResultsAlbum? {
        if (dto == null) return null

        val rawTitle = dto.title
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
            thumb = dto.thumb,
            cover_image = dto.cover_image,
            id = dto.id,
            master_id = dto.master_id
        )
    }
}