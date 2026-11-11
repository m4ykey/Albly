package com.m4ykey.search.repository

import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.repository.SearchRepositoryImpl
import com.m4ykey.search.domain.repository.SearchRepository
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class SearchRepositoryImplTest {

    private val searchService = mockk<RemoteSearchService>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(searchService)
    }

    @Test
    fun `searchAlbum should return paging data flow`() = runTest {
        val result = repository.searchAlbum(
            query = "Test Album",
            perPage = 20,
            page = 1
        )

        Assert.assertNotNull(result)
    }

    @Test
    fun `searchArtist should return paging data flow`() = runTest {
        val result = repository.searchArtist(
            query = "Test Artist",
            perPage = 20,
            page = 1
        )

        Assert.assertNotNull(result)
    }

    @Test
    fun `searchLyrics should return mapped results`() = runTest {
        val query = "Test Lyrics"



        //coEvery { lyricsService.searchLyrics(query) } returns responseDto

        val result = repository.searchLyrics(query).first()

        assertEquals(1, result.size)
        //assertEquals(responseDto.toGeniusRoot(), result.first())

        //coVerify(exactly = 1) { lyricsService.searchLyrics(query) }
    }

    @Test
    fun `searchLyrics should return empty list when request fails`() = runTest {
        val query = "Test Lyrics"

//        coEvery {
//            lyricsService.searchLyrics(query)
//        } throws RuntimeException("Network error")

        val result = repository.searchLyrics(query).first()

        assertTrue(result.isEmpty())

//        coVerify(exactly = 1) {
//            lyricsService.searchLyrics(query)
//        }
    }

    @Test
    fun `searchLyrics should propagate cancellation exception`() = runTest {
        val query = "Test lyrics"

//        coEvery {
//            lyricsService.searchLyrics(query)
//        } throws CancellationException("Request cancelled")

        assertFailsWith<CancellationException> {
            repository.searchLyrics(query).first()
        }
    }

}