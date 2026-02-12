package com.m4ykey.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.paging.SearchAlbumPagingSource
import com.m4ykey.search.data.paging.SearchArtistPagingSource
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val service : RemoteSearchService
) : SearchRepository {

    override fun searchAlbum(
        q: String,
        offset: Int,
        limit: Int,
        type: String
    ): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchAlbumPagingSource(
                    q = q,
                    service = service,
                    type = type
                )
            }
        ).flow
    }

    override fun searchArtist(
        q: String,
        offset: Int,
        limit: Int,
        type: String
    ): Flow<PagingData<ArtistItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchArtistPagingSource(
                    q = q,
                    type = type,
                    service = service
                )
            }
        ).flow
    }
}