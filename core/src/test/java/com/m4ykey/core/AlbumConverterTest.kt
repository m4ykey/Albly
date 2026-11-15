package com.m4ykey.core

import com.m4ykey.core.database.converter.AlbumConverters
import com.m4ykey.core.database.model.album.ArtistEntity
import com.m4ykey.core.database.model.album.TrackEntity
import org.junit.Test
import kotlin.test.assertEquals

class AlbumConverterTest {

    private val converters = AlbumConverters()

    private val artists = listOf(
        ArtistEntity(1, "Artist1"),
        ArtistEntity(2, "Artist2")
    )

    private val tracks = listOf(
        TrackEntity("1", "Title1", "4:32", 1),
        TrackEntity("2", "Title2", "2:21", 2)
    )

    @Test
    fun `artist list survives round trip`() {
        val json = converters.fromArtistList(artists)

        assertEquals(artists, converters.toArtistList(json))
    }

    @Test
    fun `track list survives round trip`() {
        val json = converters.fromTrackList(tracks)

        assertEquals(tracks, converters.toTrackList(json))
    }

    @Test
    fun `empty lists survive round trip`() {
        assertEquals(emptyList<ArtistEntity>(), converters.toArtistList(converters.fromArtistList(emptyList())))
        assertEquals(emptyList<TrackEntity>(), converters.toTrackList(converters.fromTrackList(emptyList())))
    }

    @Test
    fun `special characters survive round trip`() {
        val tricky = listOf(
            ArtistEntity(1, "é"),
            ArtistEntity(2, "ë"),
            ArtistEntity(3, "Ł"),
            ArtistEntity(4, "日")
        )

        assertEquals(tricky, converters.toArtistList(converters.fromArtistList(tricky)))
    }

}