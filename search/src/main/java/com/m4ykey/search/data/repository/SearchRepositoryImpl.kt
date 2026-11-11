package com.m4ykey.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.paging.SearchAlbumPagingSource
import com.m4ykey.search.data.paging.SearchArtistPagingSource
import com.m4ykey.search.data.paging.SearchLyricsPagingSource
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.lyrics.Hit
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

    override fun searchArtist(
        query: String,
        page: Int,
        perPage: Int
    ): Flow<PagingData<ResultsArtist>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchArtistPagingSource(
                    query = query,
                    service = service
                )
            }
        ).flow
    }

    override fun searchLyrics(query: String): Flow<PagingData<Hit>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchLyricsPagingSource(
                    service = service,
                    query = query
                )
            }
        ).flow
    }
}