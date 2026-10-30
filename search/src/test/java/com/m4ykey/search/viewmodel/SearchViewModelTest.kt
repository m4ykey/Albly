@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.search.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.m4ykey.search.MainDispatcherRule
import com.m4ykey.search.domain.model.lyrics.GeniusRoot
import com.m4ykey.search.domain.usecase.SearchAlbumUseCase
import com.m4ykey.search.domain.usecase.SearchArtistUseCase
import com.m4ykey.search.domain.usecase.SearchLyricsUseCase
import com.m4ykey.search.presentation.LyricsSearchState
import com.m4ykey.search.presentation.SearchType
import com.m4ykey.search.presentation.SearchTypeAction
import com.m4ykey.search.presentation.SearchUiEvent
import com.m4ykey.search.presentation.SearchViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val albumUseCase = mockk<SearchAlbumUseCase>()
    private val artistUseCase = mockk<SearchArtistUseCase>()
    private val lyricsUseCase = mockk<SearchLyricsUseCase>()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(
            artistUseCase = artistUseCase,
            lyricsUseCase = lyricsUseCase,
            albumUseCase = albumUseCase
        )
    }

    @Test
    fun `searchQuery should initially be empty`() = runTest {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `activeSearchQuery should initially be empty`() = runTest {
        assertEquals("", viewModel.activeSearchQuery.value)
    }

    @Test
    fun `updateType should update selected search type`() = runTest {
        backgroundScope.launch {
            viewModel.searchType.collect()
        }

        runCurrent()

        viewModel.updateType(SearchType.ARTIST)
        runCurrent()

        assertEquals(SearchType.ARTIST, viewModel.searchType.value)
    }

    @Test
    fun `onQueryChange should update search query`() = runTest {
        val query = "Test query"

        viewModel.onAction(SearchTypeAction.OnQueryChange(query))
        advanceUntilIdle()

        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `onSearchClick should update active search query`() = runTest {
        val query = "Test query"

        viewModel.onAction(SearchTypeAction.OnQueryChange(query))
        advanceUntilIdle()

        viewModel.onAction(SearchTypeAction.OnSearchClick)
        advanceUntilIdle()

        assertEquals(query, viewModel.activeSearchQuery.value)
    }

    @Test
    fun `onTypeClick should update type and emit event`() = runTest {
        backgroundScope.launch {
            viewModel.searchType.collect()
        }

        runCurrent()

        viewModel.searchUiEvent.test {
            val type = SearchType.ARTIST

            viewModel.onAction(SearchTypeAction.OnTypeClick(type))
            advanceUntilIdle()

            assertEquals(type, viewModel.searchType.value)
            assertEquals(SearchUiEvent.ChangeType(type), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAlbumClick should emit album click event`() = runTest {
        viewModel.searchUiEvent.test {
            val albumId = 123

            viewModel.onAction(SearchTypeAction.OnAlbumClick(albumId))
            advanceUntilIdle()

            assertEquals(SearchUiEvent.OnAlbumClick(albumId), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTrackClick should emit track click event`() = runTest {
        viewModel.searchUiEvent.test {
            val action = SearchTypeAction.OnTrackClick(
                title = "Test title",
                artist = "Test artist",
                img = "image.jpg",
                album = "Test album"
            )

            viewModel.onAction(action)
            advanceUntilIdle()

            assertEquals(
                SearchUiEvent.OnTrackClick(
                    album = action.album,
                    title = action.title,
                    img = action.img,
                    artist = action.artist
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLyricsResults should initially be idle`() = runTest {
        assertEquals(
            LyricsSearchState.Idle,
            viewModel.searchLyricsResults.value
        )
    }

    @Test
    fun `searchLyricsResults should emit Success when search succeeds`() = runTest {
        val query = "Test song"
        val songs = listOf(mockk<GeniusRoot>())

        every {
            lyricsUseCase(query)
        } returns flowOf(songs)

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            viewModel.updateType(SearchType.LYRICS)
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            runCurrent()

            assertEquals(LyricsSearchState.Loading, awaitItem())
            assertEquals(
                LyricsSearchState.Success(songs),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLyricsResults should emit Error when search fails`() = runTest {
        val query = "Test song"
        val errorMessage = "Network error"

        every {
            lyricsUseCase(query)
        } returns flow {
            throw IllegalStateException(errorMessage)
        }

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            viewModel.updateType(SearchType.LYRICS)
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            runCurrent()

            assertEquals(LyricsSearchState.Loading, awaitItem())
            assertEquals(
                LyricsSearchState.Error(errorMessage),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLyricsResults should return Idle when query is blank`() = runTest {
        viewModel.updateType(SearchType.LYRICS)

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchAlbumResults should invoke album use case for album type`() = runTest {
        val query = "Test query"

        every {
            albumUseCase(query = query, type = "album")
        } returns flowOf(PagingData.empty())

        viewModel.searchAlbumResults.test {
            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            verify(exactly = 1) {
                albumUseCase(query = query, type = "album")
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchArtistResults should invoke artist use case for artist type`() = runTest {
        val query = "Test query"

        every {
            artistUseCase(query = query, type = "artist")
        } returns flowOf(PagingData.empty())

        viewModel.searchArtistResults.test {
            viewModel.updateType(SearchType.ARTIST)

            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            verify(exactly = 1) {
                artistUseCase(query = query, type = "artist")
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchType should initially be album`() = runTest {
        backgroundScope.launch {
            viewModel.searchType.collect()
        }

        runCurrent()

        assertEquals(SearchType.ALBUM, viewModel.searchType.value)
    }

    @Test
    fun `onQueryChange should replace previous query`() = runTest {
        viewModel.onAction(SearchTypeAction.OnQueryChange("First Query"))
        advanceUntilIdle()

        viewModel.onAction(SearchTypeAction.OnQueryChange("Second query"))
        advanceUntilIdle()

        assertEquals("Second query", viewModel.searchQuery.value)
    }

    @Test
    fun `searchLyricsResults should not invoke use case when type is not lyrics`() = runTest {
        val query = "Test query"

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            verify(exactly = 0) {
                lyricsUseCase(any())
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLyricsResults should return Idle when search type changes`() = runTest {
        val query = "Test query"
        val songs = listOf(mockk<GeniusRoot>())

        every {
            lyricsUseCase(query)
        } returns flowOf(songs)

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            viewModel.updateType(SearchType.LYRICS)
            viewModel.onAction(SearchTypeAction.OnQueryChange(query))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            assertEquals(LyricsSearchState.Loading, awaitItem())
            assertEquals(LyricsSearchState.Success(songs), awaitItem())

            viewModel.updateType(SearchType.ALBUM)
            runCurrent()

            assertEquals(LyricsSearchState.Idle, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchAlbumResults should not invoke use case when type is artist`() = runTest {
        viewModel.searchAlbumResults.test {
            viewModel.updateType(SearchType.ARTIST)

            viewModel.onAction(SearchTypeAction.OnQueryChange("Test query"))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            verify(exactly = 0) {
                albumUseCase(
                    query = any<String>(),
                    type = any<String>()
                )
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchArtistResults should not invoke use case when type is album`() = runTest {
        viewModel.searchArtistResults.test {
            viewModel.onAction(SearchTypeAction.OnQueryChange("Test query"))
            advanceUntilIdle()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            advanceUntilIdle()

            verify(exactly = 0) {
                artistUseCase(
                    query = any<String>(),
                    type = any<String>()
                )
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLyricsResults should cancel previous search when query changes`() = runTest {
        val firstQuery = "First song"
        val secondQuery = "Second song"

        val firstStarted = CompletableDeferred<Unit>()
        val firstCancelled = CompletableDeferred<Unit>()

        val songs = listOf(mockk<GeniusRoot>())

        every { lyricsUseCase(firstQuery) } returns flow {
            firstStarted.complete(Unit)

            try {
                awaitCancellation()
            } finally {
                firstCancelled.complete(Unit)
            }
        }

        every { lyricsUseCase(secondQuery) } returns flowOf(songs)

        viewModel.searchLyricsResults.test {
            assertEquals(LyricsSearchState.Idle, awaitItem())

            viewModel.updateType(SearchType.LYRICS)
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnQueryChange(firstQuery))
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            runCurrent()

            firstStarted.await()

            assertEquals(LyricsSearchState.Loading, awaitItem())

            viewModel.onAction(SearchTypeAction.OnQueryChange(secondQuery))
            runCurrent()

            viewModel.onAction(SearchTypeAction.OnSearchClick)
            runCurrent()

            firstCancelled.await()

            assertEquals(
                LyricsSearchState.Success(songs),
                awaitItem()
            )

            verify(exactly = 1) {
                lyricsUseCase(firstQuery)
            }

            verify(exactly = 1) {
                lyricsUseCase(secondQuery)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}