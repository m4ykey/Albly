package com.m4ykey.search.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.search.data.mapper.toHit
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.domain.model.lyrics.Hit
import io.ktor.utils.io.CancellationException

class SearchLyricsPagingSource(
    private val service : RemoteSearchService,
    private val query : String
) : PagingSource<String, Hit>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Hit> {
        return try {
            val response = service.searchLyrics(
                query = query,
                after = params.key
            )

            LoadResult.Page(
                prevKey = null,
                nextKey = response.next,
                data = response.hits.map { it.toHit() }
            )
        } catch (e : CancellationException) {
            throw e
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Hit>): String? = null

}