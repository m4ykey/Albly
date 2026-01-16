package com.m4ykey.search.data.paging

import com.m4ykey.core.model.domain.ArtistItem
import com.m4ykey.core.model.mapper.toDomain
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.search.data.network.service.RemoteSearchService

class SearchArtistPagingSource(
    private val type : String,
    private val q : String,
    private val service : RemoteSearchService
) : BasePagingSource<ArtistItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<ArtistItem>> {
        return safeApi {
            service.searchArtist(
                q = q,
                type = type,
                limit = limit,
                offset = offset
            )
        }.map { it.artists.items?.map { artist -> artist.toDomain() } ?: emptyList() }
    }
}