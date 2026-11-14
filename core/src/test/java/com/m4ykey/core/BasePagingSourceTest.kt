package com.m4ykey.core

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.m4ykey.core.paging.BasePagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BasePagingSourceTest {

    private class FakePagingSource(
        private val onLoad : suspend (page : Int, pageSize : Int) -> Result<List<String>>
    ) : BasePagingSource<String>() {
        override suspend fun loadData(
            page: Int,
            limit: Int
        ): Result<List<String>> = onLoad(page, limit)
    }

    private fun page(
        data : List<String> = listOf("a", "b"),
        prevKey : Int? = null,
        nextKey : Int? = null
    ) = LoadResult.Page(data = data, prevKey = prevKey, nextKey = nextKey)

    private fun state(
        anchor : Int?,
        pages : List<LoadResult.Page<Int, String>>
    ) = PagingState(
        pages = pages,
        anchorPosition = anchor,
        config = PagingConfig(pageSize = 2),
        leadingPlaceholderCount = 0
    )

    private val results = Result.success(listOf("a", "b"))

    @Test
    fun `refresh with null key starts from page one`() = runTest {
        var receivedPage = -1
        var receivedLimit = -1

        val source = FakePagingSource { page, limit ->
            receivedPage = page
            receivedLimit = limit
            results
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 2,
                key = null,
                placeholdersEnabled = false
            )
        )

        assertTrue("Expected Page, but was $result", result is LoadResult.Page)
        val loadedPage = result as LoadResult.Page

        assertEquals(1, receivedPage)
        assertEquals(2, receivedLimit)
        assertEquals(listOf("a", "b"), loadedPage.data)
    }

    @Test
    fun `full page has next key`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 2,
                key = null,
                placeholdersEnabled = false
            )
        )

        assertTrue("Expected Page, but was $result", result is LoadResult.Page)
        val loadedPage = result as LoadResult.Page

        assertEquals(2, loadedPage.nextKey)
        assertNull(loadedPage.prevKey)
    }

    @Test
    fun `short page has no next key`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 3,
                key = null,
                placeholdersEnabled = false
            )
        )
        assertTrue("Expected Page, but was $result", result is LoadResult.Page)
        val loadedPage = result as LoadResult.Page

        assertNull(loadedPage.prevKey)
        assertNull(loadedPage.nextKey)
    }

    @Test
    fun `append page has prev key`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }

        val result = source.load(
            LoadParams.Append(
                placeholdersEnabled = false,
                key = 2,
                loadSize = 2
            )
        )

        assertTrue("Expected Page, but was $result", result is LoadResult.Page)
        val loadedPage = result as LoadResult.Page

        assertEquals(1, loadedPage.prevKey)
        assertEquals(3, loadedPage.nextKey)
    }
}