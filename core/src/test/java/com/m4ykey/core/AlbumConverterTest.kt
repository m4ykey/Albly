package com.m4ykey.core

import com.m4ykey.core.database.converter.AlbumConverters
import com.m4ykey.core.database.model.album.AlbumArtistEntity
import com.m4ykey.core.database.model.album.TrackEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumConverterTest {

    private val converters = AlbumConverters()

    private val artists = listOf(
        AlbumArtistEntity(1, "Artist1"),
        AlbumArtistEntity(2, "Artist2")
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
        assertEquals(emptyList<AlbumArtistEntity>(), converters.toArtistList(converters.fromArtistList(emptyList())))
        assertEquals(emptyList<TrackEntity>(), converters.toTrackList(converters.fromTrackList(emptyList())))
    }

    @Test
    fun `special characters survive round trip`() {
        val tricky = listOf(
            AlbumArtistEntity(1, "é"),
            AlbumArtistEntity(2, "ë"),
            AlbumArtistEntity(3, "Ł"),
            AlbumArtistEntity(4, "日")
        )

        assertEquals(tricky, converters.toArtistList(converters.fromArtistList(tricky)))
    }

}