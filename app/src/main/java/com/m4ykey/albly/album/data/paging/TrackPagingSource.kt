package com.m4ykey.albly.album.data.paging

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource

class TrackPagingSource(
    private val id : String,
    private val service : RemoteAlbumService
) : BasePagingSource<TrackItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<TrackItem>> {
        return safeApi {
            service.getAlbumTracks(
                limit = limit,
                offset = offset,
                id = id
            )
        }.map { it.items.map { track -> track.toDomain() } }
    }

}