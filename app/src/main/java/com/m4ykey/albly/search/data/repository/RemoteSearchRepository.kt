package com.m4ykey.albly.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.search.data.network.api.SearchApi
import com.m4ykey.albly.search.data.paging.SearchPagingSource
import com.m4ykey.albly.search.domain.model.Search
import com.m4ykey.albly.search.domain.repository.SearchRepository
import com.m4ykey.albly.util.paging.pagingConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteSearchRepository @Inject constructor(
    private val api : SearchApi,
    private val tokenProvider : SpotiTokenProvider,
    private val dispatcherIO : CoroutineDispatcher
) : SearchRepository {

    override suspend fun search(
        query: String,
        offset: Int,
        limit: Int
    ): Flow<PagingData<Search>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchPagingSource(
                    api = api,
                    query = query,
                    tokenProvider = tokenProvider
                )
            }
        ).flow.flowOn(dispatcherIO)
    }
}