package com.m4ykey.albly.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.search.data.network.service.RemoteSearchService
import com.m4ykey.albly.search.data.paging.SearchPagingSource
import com.m4ykey.albly.search.domain.repository.SearchRepository
import com.m4ykey.core.paging.pagingConfig
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