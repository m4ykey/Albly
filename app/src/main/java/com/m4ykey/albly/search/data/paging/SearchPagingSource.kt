package com.m4ykey.albly.search.data.paging

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.getToken
import com.m4ykey.albly.search.data.network.SearchService
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.BasePagingSource

class SearchPagingSource(
    private val service : SearchService,
    private val type : String,
    private val token : SpotiTokenProvider,
    private val q : String
) : BasePagingSource<AlbumItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<AlbumItem>> {
        val authToken = getToken(token)
        return safeApi {
            service.search(
                q = q,
                type = type,
                limit = limit,
                offset = offset,
                token = authToken
            )
        }.map { it.albums.items?.map { album -> album.toDomain() } ?: emptyList() }
    }
}