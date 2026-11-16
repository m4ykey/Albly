package com.m4ykey.core

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.m4ykey.core.paging.BasePagingSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

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

    @Test
    fun `failure result returns Error`() = runTest {
        val error = IOException("error")
        val source = FakePagingSource { _, _ ->
            Result.failure(error)
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 2,
                key = null,
                placeholdersEnabled = false
            )
        )

        assertTrue("Expected Error, but was $result", result is LoadResult.Error)
        assertSame(error, (result as LoadResult.Error).throwable)
    }

    @Test
    fun `thrown exception returns error`() = runTest {
        val source = FakePagingSource { _, _ ->
            throw IllegalStateException("error")
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 2,
                key = null,
                placeholdersEnabled = false
            )
        )

        assertTrue("Expected Error, but was $result", result is LoadResult.Error)
    }

    @Test
    fun `empty list has no next key`() = runTest {
        val source = FakePagingSource { _, _ ->
            Result.success(emptyList())
        }

        val result = source.load(
            LoadParams.Refresh(
                loadSize = 2,
                key = null,
                placeholdersEnabled = false
            )
        )

        val loadedPage = result as LoadResult.Page
        assertNull(loadedPage.nextKey)
        assertNull(loadedPage.prevKey)
    }

    @Test
    fun `append on page one has no prev key`() = runTest {
        val source = FakePagingSource { _, _ -> results }

        val result = source.load(
            LoadParams.Append(
                loadSize = 2,
                key = 1,
                placeholdersEnabled = false
            )
        )

        assertNull((result as LoadResult.Page).prevKey)
    }

    @Test
    fun `refresh key is null without anchor`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }

        assertNull(source.getRefreshKey(state(anchor = null, pages = listOf(page()))))
    }

    @Test
    fun `refresh key uses prevKey plus one`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }
        val pages = listOf(page(prevKey = 1, nextKey = 3))

        assertEquals(2, source.getRefreshKey(state(anchor = 0, pages = pages)))
    }

    @Test
    fun `refresh key falls back to nextKey minus one`() = runTest {
        val source = FakePagingSource { _, _ ->
            results
        }
        val pages = listOf(page(prevKey = null, nextKey = 2))
        assertEquals(1, source.getRefreshKey(state(anchor = 0, pages = pages)))
    }

    @Test
    fun `cancellation is rethrown`() = runTest {
        val source = FakePagingSource { _, _ -> throw CancellationException("cancelled") }

        assertFailsWith<CancellationException> {
            source.load(
                LoadParams.Refresh(
                    loadSize = 2,
                    key = null,
                    placeholdersEnabled = false
                )
            )
        }
    }
}