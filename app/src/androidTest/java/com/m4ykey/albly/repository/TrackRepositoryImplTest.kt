package com.m4ykey.albly.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.albly.album.data.local.model.ArtistEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.albly.core.database.AppDatabase
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.albly.track.data.network.service.RemoteTrackService
import com.m4ykey.albly.track.data.repository.TrackRepositoryImpl
import com.m4ykey.albly.track.domain.repository.TrackRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackRepositoryImplTest {

    private lateinit var db : AppDatabase
    private lateinit var repository : TrackRepository
    private lateinit var service : RemoteTrackService

    val tracks = listOf(
        TrackEntity(
            albumId = "",
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
        )
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        service = mockk()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        repository = TrackRepositoryImpl(service, db.trackDao())
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTrackById_shouldReturnCorrectData() = runTest {
        val trackId = "123"

        repository.insertTrack(tracks)
        val result = repository.getTrackById(trackId)

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Track1", result[0].name)
    }

    @Test
    fun deleteTrackById_shouldRemoveDataFromDb() = runTest {
        val trackId = "123"

        repository.insertTrack(tracks)

        repository.deleteTracksById(trackId)
        val result = repository.getTrackById(trackId)

        Assert.assertEquals(0, result.size)
    }

}