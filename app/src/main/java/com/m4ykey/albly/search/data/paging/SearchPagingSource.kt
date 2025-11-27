package com.m4ykey.albly.search.data.paging

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.search.data.network.service.RemoteSearchService
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.BasePagingSource

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