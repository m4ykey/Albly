package com.m4ykey.search.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.repository.SearchRepository
import com.m4ykey.search.domain.usecase.SearchArtistUseCase
import com.m4ykey.search.presentation.SearchType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

@Suppress("UnusedFlow")
class SearchArtistUseCaseTest {

    private val repository = mockk<SearchRepository>()
    private val useCase = SearchArtistUseCase(repository)

    @Test
    fun `invoke should call repository search with correct parameters`() {
        val query = "Test Artist"
        val page = 1
        val perPage = 20

        val expectedFlow = flowOf(PagingData.empty<ResultsArtist>())

        every {
            repository.searchArtist(query = query, perPage = perPage, page = page)
        } returns expectedFlow

        val result = useCase(perPage = perPage, page = page, query = query, type = SearchType.ALBUM.name)

        assertSame(expectedFlow, result)

        verify {
            repository.searchArtist(query = query, perPage = perPage, page = page)
        }
    }

    @Test
    fun `invoke should use default pagination parameters`() {
        val query = "Test Artist"
        val expectedFlow = flowOf(PagingData.empty<ResultsArtist>())

        every {
            repository.searchArtist(query = query, page = 1, perPage = 20)
        } returns expectedFlow

        val result = useCase(query = query, type = SearchType.ARTIST.name)

        assertSame(expectedFlow, result)

        verify(exactly = 1) {
            repository.searchArtist(query = query, page = 1, perPage = 20)
        }
    }
}