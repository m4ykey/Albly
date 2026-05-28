package com.m4ykey.search.data.paging

import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.search.data.mapper.SearchMapper
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.domain.model.search.artist.ResultsArtist

class SearchArtistPagingSource(
    private val service : RemoteSearchService,
    private val query : String
) : BasePagingSource<ResultsArtist>() {

    override suspend fun loadData(
        page: Int,
        limit: Int
    ): Result<List<ResultsArtist>> {
        return safeApi {
            service.searchArtist(
                page = page,
                query = query
            )
        }.map { response ->
            SearchMapper.mapToArtistDomain(response).results
        }
    }

}