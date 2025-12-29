package com.m4ykey.albly.use_case

import com.m4ykey.albly.album.data.local.model.ArtistEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.albly.track.domain.repository.TrackRepository
import com.m4ykey.albly.track.domain.use_case.SaveTracksUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveTracksUseCaseTest {

    private val repository = mockk<TrackRepository>(relaxed = true)
    private val useCase = SaveTracksUseCase(repository)

    @Test
    fun whenListIsEmptyRepositoryInsertShouldBeCalled() = runTest {
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
            ),
            TrackEntity(
                albumId = "",
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

        useCase.invoke(tracks)

        coVerify(exactly = 1) { repository.insertTrack(any()) }
    }

    @Test
    fun whenListIsEmptyRepositoryInsertShouldNotBeCalled() = runTest {
        val tracks = emptyList<TrackEntity>()

        useCase.invoke(tracks)

        coVerify(exactly = 0) { repository.insertTrack(any()) }
    }

}