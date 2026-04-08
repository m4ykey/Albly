package com.m4ykey.search.data.mapper

import com.m4ykey.search.data.network.model.dto.ResultsAlbumDto
import com.m4ykey.search.data.network.model.dto.SearchAlbumRootDto
import com.m4ykey.search.domain.model.search.ResultsAlbum
import com.m4ykey.search.domain.model.search.SearchAlbumRoot

object SearchMapper {

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