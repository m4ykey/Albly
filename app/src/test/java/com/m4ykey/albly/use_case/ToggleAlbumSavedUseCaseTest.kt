package com.m4ykey.albly.use_case

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.album.domain.use_case.ToggleAlbumSavedUseCase
import com.m4ykey.albly.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.albly.track.domain.use_case.SaveTracksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleAlbumSavedUseCaseTest {

    private val repository = mockk<AlbumRepository>(relaxed = true)
    private val saveTracksUseCase = mockk<SaveTracksUseCase>(relaxed = true)
    private val deleteTracksUseCase = mockk<DeleteTracksUseCase>(relaxed = true)

    private val useCase = ToggleAlbumSavedUseCase(
        repository, saveTracksUseCase, deleteTracksUseCase
    )

    @Test
    fun whenAlbumIsSavedAndNotInListenLaterTracksShouldBeDeleted() = runTest {
        val album = AlbumEntity(
            id = "123",
            albumType = "",
            artists = emptyList(),
            externalUrls = ExternalUrlsEntity(spotify = ""),
            name = "Album1",
            copyrights = emptyList(),
            images = emptyList(),
            label = "",
            releaseDate = "",
            totalTracks = 0
        )
        val isCurrentlySaved = true

        coEvery { repository.getAlbumWithStates("123") } returns mockk {
            every { isListenLaterSaved } returns null
        }

        useCase(album, isCurrentlySaved, emptyList())

        coVerify { deleteTracksUseCase("123") }
    }

}