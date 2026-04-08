package com.m4ykey.core.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {

    protected abstract suspend fun loadData(page : Int, limit : Int) : Result<List<Value>>

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val page = params.key ?: 1
        val limit = params.loadSize

        return try {
            val result = loadData(page = page, limit)

            result.fold(
                onSuccess = { data ->
                    val isLastPage = data.size < limit

                    LoadResult.Page(
                        data = data,
                        nextKey = if (isLastPage) null else page + 1,
                        prevKey = if (page == 1) null else page - 1
                    )
                },
                onFailure = { exception ->
                    LoadResult.Error(exception)
                }
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}