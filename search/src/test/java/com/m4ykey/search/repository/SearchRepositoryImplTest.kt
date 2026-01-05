package com.m4ykey.search.repository

import com.m4ykey.albly.search.data.network.service.RemoteSearchService
import com.m4ykey.albly.search.data.repository.SearchRepositoryImpl
import com.m4ykey.albly.search.domain.repository.SearchRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private val service = mockk<RemoteSearchService>()
    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(service)
    }

    @Test
    fun searchShouldReturnPagingDataFlow() = runTest {
        val query = "Daft Punk"

        val result = repository.search(
            q = query,
            offset = 0,
            limit = 20,
            type = "album"
        )

        Assert.assertNotNull(result)
    }

}