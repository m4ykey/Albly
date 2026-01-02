package com.m4ykey.track.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.albly.core.database.AppDatabase
import com.m4ykey.albly.track.data.local.dao.TrackDao
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.core.model.local.ArtistEntity
import com.m4ykey.core.model.local.ExternalUrlsEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackDaoTest {

    private lateinit var db : AppDatabase
    private lateinit var dao : TrackDao

    val tracks = listOf(
        TrackEntity(
            albumId = "123",
            durationMs = 0,
            discNumber = 0,
            name = "Track1",
            id = "123",
            explicit = false,
            previewUrl = null,
            trackNumber = 1,
            externalUrls = ExternalUrlsEntity(spotify = ""),
            artists = listOf(
                ArtistEntity(
                    id = "123",
                    name = "Artist1",
                    externalUrls = ExternalUrlsEntity(spotify = "")
                )
            )
        ),
        TrackEntity(
            albumId = "123",
            durationMs = 0,
            discNumber = 0,
            name = "Track2",
            id = "456",
            explicit = false,
            previewUrl = null,
            trackNumber = 1,
            externalUrls = ExternalUrlsEntity(spotify = ""),
            artists = listOf(
                ArtistEntity(
                    id = "123",
                    name = "Artist2",
                    externalUrls = ExternalUrlsEntity(spotify = "")
                )
            )
        )
    )

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.trackDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetTracksByAlbumId() = runTest {
        val albumId = "123"

        dao.insertTrack(tracks)

        val result = dao.getTrackById(id = albumId)

        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.all { it.albumId == albumId })
    }

    @Test
    fun deleteTracksByAlbumId_shouldRemoveOnlyTargetTracks() = runTest {
        val deleteAlbumId = "delete"
        val keepAlbum = "keep"

        val testTracks = listOf(
            tracks[0].copy(id = "T1", albumId = deleteAlbumId),
            tracks[1].copy(id = "T2", albumId = keepAlbum)
        )

        dao.insertTrack(testTracks)

        dao.deleteTracksById(deleteAlbumId)

        val remainingTarget = dao.getTrackById(deleteAlbumId)
        val remainingOther = dao.getTrackById(keepAlbum)

        Assert.assertTrue(remainingTarget.isEmpty())
        Assert.assertEquals(1, remainingOther.size)
    }
}