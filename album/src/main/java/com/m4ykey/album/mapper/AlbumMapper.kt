package com.m4ykey.album.mapper

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.ArtistEntity
import com.m4ykey.album.data.local.model.TrackEntity
import com.m4ykey.album.data.network.model.AlbumRootDto
import com.m4ykey.album.data.network.model.ArtistsDto
import com.m4ykey.album.data.network.model.ImageDto
import com.m4ykey.album.data.network.model.TrackListDto
import com.m4ykey.album.domain.model.AlbumRoot
import com.m4ykey.album.domain.model.Artists
import com.m4ykey.album.domain.model.Image
import com.m4ykey.album.domain.model.TrackList

object AlbumMapper {

    private fun mapToArtistsList(entities : List<ArtistEntity>?) : List<Artists> {
        return entities?.map { entity ->
            Artists(entity.name)
        } ?: emptyList()
    }

    private fun mapToTrackList(entities : List<TrackEntity>?) : List<TrackList> {
        return entities?.map { entity ->
            TrackList(
                duration = entity.duration,
                title = entity.title,
                position = entity.position,
                type = ""
            )
        } ?: emptyList()
    }

    private fun mapToImageList(imagePath : String?) : List<Image> {
        return if (imagePath.isNullOrEmpty()) {
            emptyList()
        } else {
            listOf(
                Image(
                    type = "primary",
                    uri = imagePath
                )
            )
        }
    }

    fun mapToDomain(entity : AlbumEntity?) : AlbumRoot? {
        if (entity == null) return null

        return AlbumRoot(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            tracklist = mapToTrackList(entity.trackList),
            artists = mapToArtistsList(entity.artistList),
            images = mapToImageList(entity.image)
        )
    }

    fun mapToDomain(dto : AlbumRootDto?) : AlbumRoot? {
        if (dto == null) return null

        return AlbumRoot(
            id = dto.id ?: 0,
            title = dto.title.orEmpty(),
            year = dto.year ?: 0,
            images = mapToImageList(dto.images),
            artists = mapToArtistsList(dto.artists),
            tracklist = mapToTrackList(dto.tracklist)
        )
    }

    private fun mapToImageList(dto : List<ImageDto>?) : List<Image> {
        return dto?.map { img ->
            Image(type = img.type.orEmpty(), uri = img.uri.orEmpty())
        } ?: emptyList()
    }

    private fun mapToTrackList(dto : List<TrackListDto>?) : List<TrackList> {
        return dto?.mapNotNull { mapToSingleTrack(it) } ?: emptyList()
    }

    private fun mapToArtistsList(dto : List<ArtistsDto>?) : List<Artists> {
        return dto?.map { a ->
            Artists(name = a.name.orEmpty())
        } ?: emptyList()
    }

    private fun mapToSingleTrack(dto : TrackListDto?) : TrackList? {
        if (dto == null) return null

        return TrackList(
            position = dto.position.orEmpty(),
            type = dto.type_.orEmpty(),
            title = dto.title.orEmpty(),
            duration = dto.duration.orEmpty()
        )
    }
}