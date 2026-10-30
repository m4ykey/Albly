package com.m4ykey.search.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.repository.SearchRepository
import com.m4ykey.search.domain.usecase.SearchAlbumUseCase
import com.m4ykey.search.presentation.SearchType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

@Suppress("UnusedFlow")
class SearchAlbumUseCaseTest {

    private val repository = mockk<SearchRepository>()
    private val useCase = SearchAlbumUseCase(repository)

    @Test
    fun `invoke should call repository search with correct parameters`() {
        val query = "Test Album"
        val perPage = 20
        val page = 1

        val expectedFlow = flowOf(PagingData.empty<ResultsAlbum>())

        every {
            repository.searchAlbum(query = query, perPage = perPage, page = page)
        } returns expectedFlow

        val result = useCase(perPage = perPage, page = page, query = query, type = SearchType.ALBUM.name)

        assertSame(expectedFlow, result)

        verify(exactly = 1) {
            repository.searchAlbum(query = query, perPage = perPage, page = page)
        }
    }

    @Test
    fun `invoke should use default pagination parameters`() {
        val query = "Test Album"
        val expectedFlow = flowOf(PagingData.empty<ResultsAlbum>())

        every {
            repository.searchAlbum(query = query, perPage = 20, page = 1)
        } returns expectedFlow

        val result = useCase(query = query, type = SearchType.ALBUM.name)

        assertSame(expectedFlow, result)

        verify(exactly = 1) {
            repository.searchAlbum(query = query, perPage = 20, page = 1)
        }
    }
}