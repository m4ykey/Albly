package com.m4ykey.search.data.paging

import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.core.model.mapper.toDomain
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.search.data.network.service.RemoteSearchService

class SearchPagingSource(
    private val type : String,
    private val q : String,
    private val service : RemoteSearchService
) : BasePagingSource<AlbumItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<AlbumItem>> {
        return safeApi {
            service.search(
                q = q,
                type = type,
                limit = limit,
                offset = offset
            )
        }.map { it.albums.items?.map { album -> album.toDomain() } ?: emptyList() }
    }
}