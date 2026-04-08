package com.m4ykey.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.paging.SearchAlbumPagingSource
import com.m4ykey.search.domain.model.search.ResultsAlbum
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val service : RemoteSearchService
) : SearchRepository {

    override fun searchAlbum(
        query: String,
        page : Int,
        perPage : Int
    ): Flow<PagingData<ResultsAlbum>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchAlbumPagingSource(
                    query = query,
                    service = service
                )
            }
        ).flow
    }
}