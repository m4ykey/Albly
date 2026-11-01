package com.m4ykey.artist.data.paging

import com.m4ykey.artist.data.mapper.toReleaseItem
import com.m4ykey.artist.data.service.RemoteArtistService
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource

class ArtistReleasePagingSource(
    private val id : Int,
    private val service : RemoteArtistService
) : BasePagingSource<ReleaseItem>() {

    override suspend fun loadData(
        page: Int,
        limit: Int
    ): Result<List<ReleaseItem>> {
        return safeApi {
            service.getArtistReleases(id = id, page = page, perPage = limit)
        }.map { response ->
            response.releases.map { it.toReleaseItem() }
        }
    }
}