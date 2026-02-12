package com.m4ykey.album.data.paging

import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.core.model.mapper.toDomain
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource

class NewReleasePaging(
    private val service : RemoteAlbumService
) : BasePagingSource<AlbumItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<AlbumItem>> {
        return safeApi {
            service.getNewRelease(
                limit = limit,
                offset = offset
            )
        }.map { it.albums.items?.map { item ->  item.toDomain() } ?: emptyList() }
    }

}