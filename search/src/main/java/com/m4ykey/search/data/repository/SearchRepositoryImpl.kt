package com.m4ykey.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.paging.SearchPagingSource
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val service : RemoteSearchService
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
                    service = service,
                    type = type
                )
            }
        ).flow
    }
}