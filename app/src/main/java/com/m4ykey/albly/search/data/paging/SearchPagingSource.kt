package com.m4ykey.albly.search.data.paging

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.artist.data.mapper.toDomain
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.getToken
import com.m4ykey.albly.search.data.network.api.SearchApi
import com.m4ykey.albly.search.domain.model.Search
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.BasePagingSource

class SearchPagingSource(
    private val api : SearchApi,
    private val tokenProvider : SpotiTokenProvider,
    private val query : String
) : BasePagingSource<Search>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<Search>> {
        return safeApi {
            api.search(
                token = getToken(tokenProvider),
                query = query,
                limit = limit,
                offset = offset
            )
        }.map { response ->
            listOf(
                Search(
                    albums = response.albums.toDomain(),
                    artists = response.artists.toDomain()
                )
            )
        }
    }
}