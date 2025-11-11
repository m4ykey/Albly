package com.m4ykey.albly.album.data.paging

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.getToken
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.BasePagingSource

class TrackPagingSource(
    private val token : SpotiTokenProvider,
    private val service : AlbumService,
    private val id : String
) : BasePagingSource<TrackItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<TrackItem>> {
        val authToken = getToken(token)
        return safeApi {
            service.getAlbumTracks(
                limit = limit,
                offset = offset,
                id = id,
                token = authToken
            )
        }.map { it.items.map { track -> track.toDomain() } }
    }
}