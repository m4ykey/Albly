package com.m4ykey.albly.use_case

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.search.domain.repository.SearchRepository
import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SearchUseCaseTest {

    private val repository = mockk<SearchRepository>()
    private val useCase = SearchUseCase(repository)

    @Test
    fun invokeShouldCallRepositorySearchWithCorrectParameters() = runTest {
        val q = "Daft Punk"
        val type = "album"
        val limit = 20
        val offset = 0

        val expectedFlow = flowOf(PagingData.empty<AlbumItem>())

        every {
            repository.search(q, offset, limit, type)
        } returns expectedFlow

        val result = useCase(limit, offset, q, type)

        assertEquals(expectedFlow, result)

        verify(exactly = 1) {
            repository.search(q, offset, limit, type)
        }
    }

}