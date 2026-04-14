package com.m4ykey.album.mapper

import com.m4ykey.album.data.network.model.AlbumRootDto
import com.m4ykey.album.data.network.model.ArtistsDto
import com.m4ykey.album.data.network.model.ImageDto
import com.m4ykey.album.data.network.model.TrackListDto
import com.m4ykey.album.domain.model.AlbumRoot
import com.m4ykey.album.domain.model.Artists
import com.m4ykey.album.domain.model.Image
import com.m4ykey.album.domain.model.TrackList

object AlbumMapper {

    fun mapToDomain(dto : AlbumRootDto?) : AlbumRoot? {
        if (dto == null) return null

        return AlbumRoot(
            id = dto.id,
            title = dto.title,
            year = dto.year,
            images = mapToImageList(dto.images),
            artists = mapToArtistsList(dto.artists),
            trackList = mapToTrackList(dto.trackList)
        )
    }

    private fun mapToImageList(dto : List<ImageDto>?) : List<Image> {
        return dto?.map { img ->
            Image(type = img.type, uri = img.uri)
        } ?: emptyList()
    }

    private fun mapToTrackList(dto : List<TrackListDto>?) : List<TrackList> {
        return dto?.mapNotNull { mapToSingleTrack(it) } ?: emptyList()
    }

    private fun mapToArtistsList(dto : List<ArtistsDto>?) : List<Artists> {
        return dto?.map { a ->
            Artists(name = a.name, thumbnailUrl = a.thumbnail_url)
        } ?: emptyList()
    }

    private fun mapToSingleTrack(dto : TrackListDto?) : TrackList? {
        if (dto == null) return null

        return TrackList(
            position = dto.position,
            type = dto.type_,
            title = dto.title,
            duration = dto.duration
        )
    }
}