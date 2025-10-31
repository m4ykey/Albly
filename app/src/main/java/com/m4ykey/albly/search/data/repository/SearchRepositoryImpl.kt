package com.m4ykey.albly.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.search.data.network.SearchService
import com.m4ykey.albly.search.data.paging.SearchPagingSource
import com.m4ykey.albly.search.domain.repository.SearchRepository
import com.m4ykey.albly.util.paging.pagingConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dispatcherIO : CoroutineDispatcher,
    private val service : SearchService,
    private val token : SpotiTokenProvider
) : SearchRepository {

    override fun search(
        q: String,
        offset: Int,
        limit: Int,
        type: String
    ): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchPagingSource(
                    q = q,
                    token = token,
                    service = service,
                    type = type
                )
            }
        ).flow.flowOn(dispatcherIO)
    }
}