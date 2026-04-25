package com.m4ykey.album.mapper

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.ArtistEntity
import com.m4ykey.album.data.local.model.TrackEntity
import com.m4ykey.album.data.network.model.detail.AlbumRootDto
import com.m4ykey.album.data.network.model.detail.ArtistsDto
import com.m4ykey.album.data.network.model.detail.ImageDto
import com.m4ykey.album.data.network.model.detail.TrackListDto
import com.m4ykey.album.data.network.model.new_release.NewReleaseResultDto
import com.m4ykey.album.data.network.model.new_release.NewReleaseRootDto
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.model.detail.Artists
import com.m4ykey.album.domain.model.detail.Image
import com.m4ykey.album.domain.model.detail.TrackList
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.model.new_release.NewReleaseRoot

object AlbumMapper {

    fun mapToNewRelease(dto : NewReleaseRootDto?) : NewReleaseRoot {
        val results = dto?.results?.mapNotNull { mapToSingleNewRelease(it) } ?: emptyList()
        return NewReleaseRoot(results)
    }

    fun mapToSingleNewRelease(dto : NewReleaseResultDto?) : NewReleaseResult? {
        if (dto == null) return null

        val rawTitle = dto.title
        var artist = "Unknown artist"
        var album = rawTitle

        rawTitle?.takeIf { it.contains(" - ") }?.let {
            val parts = it.split(" - ", limit = 2)
            artist = parts[0].trim()
            album = parts[1].trim()
        }

        return NewReleaseResult(
            title = album.orEmpty(),
            master_id = dto.master_id ?: 0,
            thumb = dto.thumb.orEmpty(),
            cover_image = dto.cover_image.orEmpty(),
            artist = artist
        )
    }

    fun mapToEntity(domain : AlbumRoot?) : AlbumEntity? {
        if (domain == null) return null

        return AlbumEntity(
            id = domain.id,
            title = domain.title,
            year = domain.year,
            save_time = System.currentTimeMillis(),
            image = domain.images.firstOrNull()?.uri.orEmpty(),
            artistList = mapDomainArtistsToEntity(domain.artists),
            trackList = mapDomainTrackListToEntity(domain.tracklist)
        )
    }

    private fun mapDomainArtistsToEntity(artists : List<Artists>) : List<ArtistEntity> {
        return artists.map { artists ->
            ArtistEntity(
                name = artists.name,
                artist_id = 0
            )
        }
    }

    private fun mapDomainTrackListToEntity(tracks : List<TrackList>) : List<TrackEntity> {
        return tracks.map { track ->
            TrackEntity(
                position = track.position,
                duration = track.duration,
                title = track.title,
                track_id = 0
            )
        }
    }

    fun mapToDomain(entity : AlbumEntity?) : AlbumRoot? {
        if (entity == null) return null

        return AlbumRoot(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            tracklist = mapEntityToTrackList(entity.trackList),
            artists = mapEntityToArtistsList(entity.artistList),
            images = mapStringPathToImageList(entity.image)
        )
    }

    private fun mapEntityToArtistsList(entities : List<ArtistEntity>?) : List<Artists> {
        return entities?.map { entity ->
            Artists(entity.name)
        } ?: emptyList()
    }

    private fun mapEntityToTrackList(entities : List<TrackEntity>?) : List<TrackList> {
        return entities?.map { entity ->
            TrackList(
                duration = entity.duration,
                title = entity.title,
                position = entity.position,
                type = ""
            )
        } ?: emptyList()
    }

    private fun mapStringPathToImageList(imagePath : String?) : List<Image> {
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

    fun mapToDomain(dto : AlbumRootDto?) : AlbumRoot? {
        if (dto == null) return null

        return AlbumRoot(
            id = dto.id ?: 0,
            title = dto.title.orEmpty(),
            year = dto.year ?: 0,
            images = mapDtoToImageList(dto.images),
            artists = mapDtoToArtistsList(dto.artists),
            tracklist = mapDtoToTrackList(dto.tracklist)
        )
    }

    private fun mapDtoToImageList(dto : List<ImageDto>?) : List<Image> {
        return dto?.map { img ->
            Image(type = img.type.orEmpty(), uri = img.uri.orEmpty())
        } ?: emptyList()
    }

    private fun mapDtoToTrackList(dto : List<TrackListDto>?) : List<TrackList> {
        return dto?.mapNotNull { mapToSingleTrack(it) } ?: emptyList()
    }

    private fun mapDtoToArtistsList(dto : List<ArtistsDto>?) : List<Artists> {
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