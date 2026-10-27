package com.m4ykey.search.data.paging

import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.search.data.mapper.toResultsAlbum
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.domain.model.album.ResultsAlbum

class SearchAlbumPagingSource(
    private val service : RemoteSearchService,
    private val query : String
) : BasePagingSource<ResultsAlbum>() {

    override suspend fun loadData(
        page: Int,
        limit: Int
    ): Result<List<ResultsAlbum>> {
        return safeApi {
            service.searchAlbum(
                page = page,
                query = query,
                perPage = limit
            )
        }.map { response ->
            response.results.map { it.toResultsAlbum() }
        }
    }
}