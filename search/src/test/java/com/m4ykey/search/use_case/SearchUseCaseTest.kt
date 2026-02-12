package com.m4ykey.search.use_case

import androidx.paging.PagingData
import com.m4ykey.search.domain.repository.SearchRepository
import com.m4ykey.search.domain.use_case.SearchAlbumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchUseCaseTest {

    private val repository = mockk<SearchRepository>()
    private val useCase = SearchAlbumUseCase(repository)

    @Test
    fun invokeShouldCallRepositorySearchWithCorrectParameters() = runTest {
        val q = "Daft Punk"
        val type = "album"
        val limit = 20
        val offset = 0

        val expectedFlow = flowOf(PagingData.empty<AlbumItem>())

        every {
            repository.searchAlbum(q, offset, limit, type)
        } returns expectedFlow

        val result = useCase(limit, offset, q, type)

        assertEquals(expectedFlow, result)

        verify(exactly = 1) {
            repository.searchAlbum(q, offset, limit, type)
        }
    }

}