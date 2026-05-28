package com.m4ykey.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.search.data.mapper.SearchMapper
import com.m4ykey.search.data.network.service.RemoteSearchLyricsService
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.paging.SearchAlbumPagingSource
import com.m4ykey.search.data.paging.SearchArtistPagingSource
import com.m4ykey.search.domain.model.search.album.ResultsAlbum
import com.m4ykey.search.domain.model.search.artist.ResultsArtist
import com.m4ykey.search.domain.model.search.lyrics.GeniusRoot
import com.m4ykey.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val service : RemoteSearchService,
    private val lyricsService : RemoteSearchLyricsService
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

    override fun searchLyrics(query: String): Flow<List<GeniusRoot>> {
        return flow {
            try {
                val responseDto = lyricsService.searchLyrics(query)
                val domainResults = SearchMapper.mapToGeniusRootDomain(responseDto)
                emit(listOf(domainResults))
            } catch (e : Exception) {
                emit(emptyList())
            }
        }
    }
}