@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.track.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.m4ykey.core.model.domain.ExternalUrls
import com.m4ykey.track.domain.model.TrackItem
import com.m4ykey.track.domain.use_case.GetTrackUseCase
import com.m4ykey.track.domain.use_case.TrackUseCase
import com.m4ykey.track.presentation.TrackViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TrackViewModelTest {

    private val useCase = mockk<TrackUseCase>()
    private val getTrackUseCase = mockk<GetTrackUseCase>()
    private lateinit var viewModel : TrackViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TrackViewModel(
            useCase = useCase,
            getTrackUseCase = getTrackUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val track = listOf(
        TrackItem(
            id = "1",
            name = "Track 1",
            artists = emptyList(),
            discNumber = 0,
            explicit = false,
            durationMs = 0,
            externalUrls = ExternalUrls(spotify = ""),
            previewUrl = "",
            trackNumber = 1
        ),
        TrackItem(
            id = "2",
            name = "Track 2",
            artists = emptyList(),
            discNumber = 0,
            explicit = false,
            durationMs = 0,
            externalUrls = ExternalUrls(spotify = ""),
            previewUrl = "",
            trackNumber = 2
        )
    )

    @Test
    fun setAlbumShouldTriggerUseCaseAndReturnPagingData() = runTest {
        val albumId = "123"
        val mockPagingData = PagingData.from(track)

        coEvery { useCase.invoke(id = albumId) } returns flowOf(mockPagingData)

        viewModel.setAlbum(albumId)

        viewModel.trackResults.test {
            val result = awaitItem()
            Assert.assertNotNull(result)
        }

        coVerify { useCase.invoke(albumId) }
    }

    @Test
    fun setAlbumWithNullShouldNotTriggerUseCase() = runTest {
        coEvery { useCase.invoke(any()) } returns flowOf(PagingData.empty())

        viewModel.trackResults.test {
            viewModel.setAlbum(null)

            expectNoEvents()
        }

        coVerify(exactly = 0) { useCase.invoke(any()) }
    }

}