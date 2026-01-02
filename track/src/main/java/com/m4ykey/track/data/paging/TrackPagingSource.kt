package com.m4ykey.track.data.paging

import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.track.data.mapper.toDomain
import com.m4ykey.track.data.network.service.RemoteTrackService
import com.m4ykey.track.domain.model.TrackItem

class TrackPagingSource(
    private val id : String,
    private val service : RemoteTrackService
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