package com.m4ykey.search.use_case

import com.m4ykey.search.domain.model.lyrics.GeniusRoot
import com.m4ykey.search.domain.repository.SearchRepository
import com.m4ykey.search.domain.usecase.SearchLyricsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

@Suppress("UnusedFlow")
class SearchLyricsUseCaseTest {

    private val repository = mockk<SearchRepository>()
    private val useCase = SearchLyricsUseCase(repository)

    @Test
    fun `invoke should call repository search with correct query`() {
        val query = "Test lyrics"

        val expectedFlow = flowOf(listOf(mockk<GeniusRoot>()))

        every { repository.searchLyrics(query) } returns expectedFlow

        val result = useCase(query)

        assertSame(expectedFlow, result)

        verify(exactly = 1) {
            repository.searchLyrics(query)
        }
    }

    @Test
    fun `invoke should return empty results search flow`() {
        val query = "Unknown song"
        val expectedFlow = flowOf(emptyList<GeniusRoot>())

        every { repository.searchLyrics(query) } returns expectedFlow

        val result = useCase(query)

        assertSame(expectedFlow, result)
    }

}